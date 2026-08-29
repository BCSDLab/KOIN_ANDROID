package `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentChatException
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatMessage as DomainRecruitmentChatMessage
import `in`.koreatech.koin.domain.model.upload.PreSignedUrlDomain
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadImageUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.chat.GetRecruitmentChatMessagesUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.chat.GetRecruitmentChatRoomUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.chat.SendRecruitmentChatMessageUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.toRecruitmentChatMessageGroups
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class RecruitmentGroupChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecruitmentChatRoomUseCase: GetRecruitmentChatRoomUseCase,
    private val getRecruitmentChatMessagesUseCase: GetRecruitmentChatMessagesUseCase,
    private val sendRecruitmentChatMessageUseCase: SendRecruitmentChatMessageUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel(), ContainerHost<RecruitmentGroupChatState, RecruitmentGroupChatSideEffect> {

    override val container = container<RecruitmentGroupChatState, RecruitmentGroupChatSideEffect>(RecruitmentGroupChatState()) {
        val route = savedStateHandle.toRoute<RecruitmentNavType.RecruitmentGroupChat>()
        reduce {
            state.copy(recruitmentId = route.recruitmentId, chatRoomId = route.chatRoomId)
        }
        loadCurrentUser()
        loadChatRoom()
    }

    private var pollingJob: Job? = null
    private val loadMessagesMutex = Mutex()
    private var accumulatedMessages: List<DomainRecruitmentChatMessage> = emptyList()

    private fun loadCurrentUser() = intent {
        getUserInfoUseCase().onSuccess { user ->
            reduce {
                state.copy(
                    currentUserId = user.userId(),
                    messages = accumulatedMessages.toRecruitmentChatMessageGroups(user.userId())
                )
            }
        }
    }

    private fun loadChatRoom() = intent {
        getRecruitmentChatRoomUseCase(state.recruitmentId, state.chatRoomId)
            .onSuccess { room ->
                reduce {
                    state.copy(
                        isLoading = false,
                        title = room.roomName,
                        currentMemberCount = room.memberCount,
                        maxMemberCount = room.maxMemberCount,
                        status = room.status
                    )
                }
                loadMessages()
            }.onFailure {
                reduce { state.copy(isLoading = false) }
                postSideEffect(RecruitmentGroupChatSideEffect.FailedToLoadChatRoom)
            }
    }

    fun startPolling() {
        if (pollingJob?.isActive == true) return

        pollingJob = viewModelScope.launch {
            while (isActive) {
                loadMessages()
                delay(POLLING_INTERVAL_MS)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }

    private fun loadMessages() = intent {
        if (!loadMessagesMutex.tryLock()) return@intent

        try {
            val cursor = accumulatedMessages.lastOrNull()?.messageId
            getRecruitmentChatMessagesUseCase(
                recruitmentId = state.recruitmentId,
                chatRoomId = state.chatRoomId,
                afterMessageId = cursor,
                beforeMessageId = null
            ).onSuccess { newMessages ->
                if (newMessages.isEmpty()) return@onSuccess
                accumulatedMessages = (accumulatedMessages + newMessages).distinctBy { it.messageId }
                reduce {
                    state.copy(messages = accumulatedMessages.toRecruitmentChatMessageGroups(state.currentUserId))
                }
            }.onFailure {
                if (accumulatedMessages.isEmpty()) {
                    postSideEffect(RecruitmentGroupChatSideEffect.FailedToLoadMessages)
                }
            }
        } finally {
            loadMessagesMutex.unlock()
        }
    }

    fun onChatInputValueChange(value: String) = blockingIntent {
        reduce { state.copy(chatInputValue = value) }
    }

    fun sendMessage() = intent {
        val content = state.chatInputValue.trim()
        if (content.isEmpty()) return@intent

        sendRecruitmentChatMessageUseCase(
            recruitmentId = state.recruitmentId,
            chatRoomId = state.chatRoomId,
            content = content,
            isImage = false
        ).onSuccess {
            reduce { state.copy(chatInputValue = "") }
            loadMessages()
        }.onFailure { e ->
            postSideEffect(e.toSendMessageSideEffect())
        }
    }

    fun uploadImage(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri
    ) = intent {
        reduce { state.copy(isUploadingImage = true) }
        uploadImageUseCase(
            domain = PreSignedUrlDomain.TEAM_RECRUITMENT,
            contentLength = fileSize,
            contentType = fileType,
            fileName = fileName,
            imageUri = imageUri.toString()
        ).onSuccess { fileUrl ->
            sendRecruitmentChatMessageUseCase(
                recruitmentId = state.recruitmentId,
                chatRoomId = state.chatRoomId,
                content = fileUrl,
                isImage = true
            ).onSuccess {
                reduce { state.copy(isUploadingImage = false) }
                loadMessages()
            }.onFailure { e ->
                reduce { state.copy(isUploadingImage = false) }
                postSideEffect(e.toSendMessageSideEffect())
            }
        }.onFailure {
            reduce { state.copy(isUploadingImage = false) }
            postSideEffect(RecruitmentGroupChatSideEffect.FailedToUploadImage)
        }
    }

    private fun Throwable.toSendMessageSideEffect(): RecruitmentGroupChatSideEffect = when (this) {
        is KoinRecruitmentChatException.ChatReadOnlyException -> RecruitmentGroupChatSideEffect.ChatRoomReadOnly
        is KoinRecruitmentChatException.RequestTooFastException -> RecruitmentGroupChatSideEffect.MessageTooFast
        else -> RecruitmentGroupChatSideEffect.FailedToSendMessage
    }

    companion object {
        const val POLLING_INTERVAL_MS = 1000L
    }
}

private fun User.userId(): Int = when (this) {
    is User.Student -> id
    is User.General -> id
    User.Anonymous -> 0
}
