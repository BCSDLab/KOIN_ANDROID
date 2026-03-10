package `in`.koreatech.koin.feature.chat.ui.groupchat

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.upload.PreSignedUrlDomain
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanChatMessagesUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanPostDetailUseCase
import `in`.koreatech.koin.domain.usecase.callvan.SendCallvanMessageUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadImageUseCase
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessage
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessageGroup
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
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
class GroupChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCallvanChatMessagesUseCase: GetCallvanChatMessagesUseCase,
    private val getCallvanPostDetailUseCase: GetCallvanPostDetailUseCase,
    private val sendCallvanMessageUseCase: SendCallvanMessageUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel(), ContainerHost<GroupChatState, GroupChatSideEffect> {

    override val container = container<GroupChatState, GroupChatSideEffect>(GroupChatState()) {
        val postId: Int? = savedStateHandle[POST_ID]
        checkNotNull(postId)
        getCallvanPostDetail(postId)
    }

    private var pollingJob: Job? = null
    private val loadMessagesMutex: Mutex = Mutex()

    private fun getCallvanPostDetail(postId: Int) = intent {
        getCallvanPostDetailUseCase(postId).onSuccess { postDetail ->
            val currentUser = postDetail.participants.find { it.isMe }
            val memberColors = postDetail.participants
                .mapIndexed { index, participant -> participant.userId to (index % MAX_USER_COLORS) }
                .toMap()
                .toImmutableMap()
            reduce {
                state.copy(
                    isLoading = false,
                    postId = postId,
                    userId = currentUser?.userId ?: 0,
                    userNickname = currentUser?.nickname ?: "",
                    departure = postDetail.departure,
                    arrival = postDetail.arrival,
                    departureTime = postDetail.departureTime,
                    currentMemberCount = postDetail.currentParticipants,
                    maxMemberCount = postDetail.maxParticipants,
                    memberColors = memberColors
                )
            }
            loadMessages()
        }.onFailure {
            reduce {
                state.copy(isLoading = false)
            }
            postSideEffect(GroupChatSideEffect.FailedToLoadMessages)
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

    fun onChatInputValueChange(value: String) = blockingIntent {
        reduce {
            state.copy(chatInputValue = value)
        }
    }

    fun sendMessage() = intent {
        val message = state.chatInputValue
        if (message.isBlank()) return@intent
        val postId = state.postId ?: return@intent

        sendCallvanMessageUseCase(
            postId = postId,
            isImage = false,
            content = message
        ).onSuccess {
            reduce {
                state.copy(chatInputValue = "")
            }
            loadMessages()
        }.onFailure {
            postSideEffect(GroupChatSideEffect.FailedToSendMessage)
        }
    }

    fun uploadImage(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri
    ) = intent {
        val postId = state.postId ?: return@intent
        val imageUriString = imageUri.toString()
        val uploadId = UUID.randomUUID().toString()

        reduce {
            state.copy(
                uploadingImage = (
                    state.uploadingImage + ConvertedChatMessage(
                        userId = state.userId,
                        userNickname = state.userNickname,
                        content = imageUriString,
                        timestamp = LocalDateTime.now(),
                        isImage = true,
                        isSentByMe = true,
                        uploadId = uploadId
                    )
                    ).toImmutableList()
            )
        }
        uploadImageUseCase(
            domain = PreSignedUrlDomain.CALLVAN,
            contentLength = fileSize,
            contentType = fileType,
            fileName = fileName,
            imageUri = imageUriString
        ).onSuccess { fileUrl ->
            sendCallvanMessageUseCase(
                postId = postId,
                isImage = true,
                content = fileUrl
            ).onSuccess {
                reduce {
                    state.copy(
                        uploadingImage = state.uploadingImage
                            .filterNot { it.uploadId == uploadId }
                            .toImmutableList()
                    )
                }
                loadMessages()
            }.onFailure {
                reduce {
                    state.copy(
                        uploadingImage = state.uploadingImage
                            .filterNot { it.uploadId == uploadId }
                            .toImmutableList()
                    )
                }
                postSideEffect(GroupChatSideEffect.FailedToSendMessage)
            }
        }.onFailure {
            reduce {
                state.copy(
                    uploadingImage = state.uploadingImage
                        .filterNot { it.uploadId == uploadId }
                        .toImmutableList()
                )
            }
            postSideEffect(GroupChatSideEffect.FailedToUploadImage)
        }
    }

    fun changeShowImageState(show: Boolean, uri: Uri) = blockingIntent {
        reduce {
            state.copy(showImage = Pair(show, uri.toString()))
        }
    }

    private fun loadMessages() = intent {
        val postId = state.postId ?: return@intent
        if (!loadMessagesMutex.tryLock()) return@intent

        try {
            getCallvanChatMessagesUseCase(
                postId = postId
            ).onSuccess { chatMessage ->
                val messageGroups = chatMessage.messages.groupBy { it.date }.map { (date, messages) ->
                    GroupChatMessageGroup(
                        date = date,
                        messages = messages.mapIndexed { index, message ->
                            val prevMessage = messages.getOrNull(index - 1)
                            GroupChatMessage(
                                id = "${postId}_${date}_${message.userId}_${message.time}_$index",
                                userId = message.userId,
                                userNickname = message.senderNickname,
                                content = message.content,
                                timestamp = message.time,
                                isImage = message.isImage,
                                isSentByMe = message.isMine,
                                isFirstInGroup = prevMessage?.userId != message.userId,
                                isLeftUser = message.isLeftUser
                            )
                        }.toImmutableList()
                    )
                }.toImmutableList()
                reduce {
                    state.copy(messages = messageGroups)
                }
            }.onFailure {
                if (state.messages.isEmpty()) {
                    postSideEffect(GroupChatSideEffect.FailedToLoadMessages)
                }
            }
        } finally {
            loadMessagesMutex.unlock()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }

    companion object {
        const val MAX_USER_COLORS = 8
        const val POLLING_INTERVAL_MS = 1000L
        const val POST_ID = "post_id"
    }
}
