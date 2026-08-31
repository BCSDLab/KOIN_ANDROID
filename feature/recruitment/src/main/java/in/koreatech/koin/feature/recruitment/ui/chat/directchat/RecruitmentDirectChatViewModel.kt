package `in`.koreatech.koin.feature.recruitment.ui.chat.directchat

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentChatException
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatMessage as DomainRecruitmentChatMessage
import `in`.koreatech.koin.domain.model.upload.PreSignedUrlDomain
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadImageUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.chat.CreateOrGetRecruitmentDirectChatRoomUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.chat.GetRecruitmentChatMessagesUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.chat.SendRecruitmentChatMessageUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.toRecruitmentChatMessageGroups
import `in`.koreatech.koin.feature.recruitment.ui.chat.util.userId
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class RecruitmentDirectChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createOrGetRecruitmentDirectChatRoomUseCase: CreateOrGetRecruitmentDirectChatRoomUseCase,
    private val getRecruitmentChatMessagesUseCase: GetRecruitmentChatMessagesUseCase,
    private val sendRecruitmentChatMessageUseCase: SendRecruitmentChatMessageUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel(), ContainerHost<RecruitmentDirectChatState, RecruitmentDirectChatSideEffect> {

    override val container = container<RecruitmentDirectChatState, RecruitmentDirectChatSideEffect>(RecruitmentDirectChatState()) {
        val route = savedStateHandle.toRoute<RecruitmentNavType.RecruitmentDirectChat>()
        reduce {
            state.copy(recruitmentId = route.recruitmentId, applicationId = route.applicationId)
        }
        loadCurrentUser()
        createOrGetChatRoom()
    }

    private var pollingJob: Job? = null
    private val loadMessagesMutex = Mutex()
    private var accumulatedMessages: List<DomainRecruitmentChatMessage> = emptyList()

    private fun loadCurrentUser() = intent {
        getUserInfoUseCase().onSuccess { user ->
            val messages = loadMessagesMutex.withLock { accumulatedMessages }
            reduce {
                state.copy(
                    currentUserId = user.userId(),
                    messages = messages.toRecruitmentChatMessageGroups(user.userId())
                )
            }
        }
    }

    private fun createOrGetChatRoom() = intent {
        createOrGetRecruitmentDirectChatRoomUseCase(state.recruitmentId, state.applicationId)
            .onSuccess { room ->
                reduce {
                    state.copy(
                        isLoading = false,
                        chatRoomId = room.chatRoomId,
                        partnerNickname = room.counterpart?.nickname.orEmpty(),
                        status = room.status
                    )
                }
                loadMessages()
            }.onFailure { e ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(
                    if (e is KoinRecruitmentChatException.DirectChatConflictException) {
                        RecruitmentDirectChatSideEffect.DirectChatUnavailable
                    } else {
                        RecruitmentDirectChatSideEffect.FailedToCreateChatRoom
                    }
                )
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
        val chatRoomId = state.chatRoomId ?: return@intent
        if (!loadMessagesMutex.tryLock()) return@intent

        try {
            val cursor = accumulatedMessages.lastOrNull()?.messageId
            getRecruitmentChatMessagesUseCase(
                recruitmentId = state.recruitmentId,
                chatRoomId = chatRoomId,
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
                    postSideEffect(RecruitmentDirectChatSideEffect.FailedToLoadMessages)
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
        val chatRoomId = state.chatRoomId ?: return@intent
        val content = state.chatInputValue.trim()
        if (content.isEmpty()) return@intent

        sendRecruitmentChatMessageUseCase(
            recruitmentId = state.recruitmentId,
            chatRoomId = chatRoomId,
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
        val chatRoomId = state.chatRoomId ?: return@intent
        reduce { state.copy(uploadingImageCount = state.uploadingImageCount + 1) }

        suspend fun finishUpload() {
            reduce { state.copy(uploadingImageCount = (state.uploadingImageCount - 1).coerceAtLeast(0)) }
        }

        uploadImageUseCase(
            domain = PreSignedUrlDomain.TEAM_RECRUITMENT,
            contentLength = fileSize,
            contentType = fileType,
            fileName = fileName,
            imageUri = imageUri.toString()
        ).onSuccess { fileUrl ->
            sendRecruitmentChatMessageUseCase(
                recruitmentId = state.recruitmentId,
                chatRoomId = chatRoomId,
                content = fileUrl,
                isImage = true
            ).onSuccess {
                finishUpload()
                loadMessages()
            }.onFailure { e ->
                finishUpload()
                postSideEffect(e.toSendMessageSideEffect())
            }
        }.onFailure {
            finishUpload()
            postSideEffect(RecruitmentDirectChatSideEffect.FailedToUploadImage)
        }
    }

    private fun Throwable.toSendMessageSideEffect(): RecruitmentDirectChatSideEffect = when (this) {
        is KoinRecruitmentChatException.ChatReadOnlyException -> RecruitmentDirectChatSideEffect.ChatRoomReadOnly
        is KoinRecruitmentChatException.RequestTooFastException -> RecruitmentDirectChatSideEffect.MessageTooFast
        else -> RecruitmentDirectChatSideEffect.FailedToSendMessage
    }

    companion object {
        const val POLLING_INTERVAL_MS = 1000L
    }
}
