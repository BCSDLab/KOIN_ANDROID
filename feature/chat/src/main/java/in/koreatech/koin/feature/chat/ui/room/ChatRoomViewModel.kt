package `in`.koreatech.koin.feature.chat.ui.room

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.chat.KoinChatException
import `in`.koreatech.koin.domain.model.chat.ChatConnectionState
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.chat.ChatBlockUserUseCase
import `in`.koreatech.koin.domain.usecase.chat.ChatWSConnectUseCase
import `in`.koreatech.koin.domain.usecase.chat.ChatWSDisconnectUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatConnectionStateUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatMessageUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatRoomUseCase
import `in`.koreatech.koin.domain.usecase.chat.SendMessageUseCase
import `in`.koreatech.koin.domain.usecase.chat.SubscribeChatRoomUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetLostAndFoundPreSignedUrlUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import `in`.koreatech.koin.feature.chat.ui.model.appendMessage
import `in`.koreatech.koin.feature.chat.ui.model.mapToConvertedChatMessages
import `in`.koreatech.koin.feature.chat.ui.model.mergeWithChatMessages
import java.net.UnknownHostException
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.hildan.krossbow.stomp.LostReceiptException
import org.hildan.krossbow.websocket.WebSocketConnectionException
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatWSConnectUseCase: ChatWSConnectUseCase,
    private val chatWSDisconnectUseCase: ChatWSDisconnectUseCase,
    private val getChatRoomUseCase: GetChatRoomUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val subscribeChatRoomUseCase: SubscribeChatRoomUseCase,
    private val getChatConnectionStateUseCase: GetChatConnectionStateUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getLostAndFoundPreSignedUrlUseCase: GetLostAndFoundPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val chatBlockUserUseCase: ChatBlockUserUseCase
) : ViewModel(), ContainerHost<ChatRoomState, ChatRoomSideEffect> {
    override val container = container<ChatRoomState, ChatRoomSideEffect>(ChatRoomState(), savedStateHandle) {
        val articleId = savedStateHandle.get<Int>(ARTICLE_ID)
        val chatRoomId = savedStateHandle.get<Int>(CHAT_ROOM_ID)
        checkNotNull(articleId)
        getChatRoom(articleId, chatRoomId)
    }

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)
    private val _connectChannel = Channel<Boolean>()
    val connectChannel = _connectChannel.receiveAsFlow()

    init {
        getUserInfo()
        observeConnectionState()
    }

    private fun observeConnectionState() = intent {
        var previousState: ChatConnectionState? = null
        getChatConnectionStateUseCase().collect { current ->
            if (previousState == ChatConnectionState.RECONNECTING && current == ChatConnectionState.CONNECTED) {
                getChatMessageUseCase(state.articleId, state.chatRoomId).onSuccess { messages ->
                    reduce {
                        state.copy(
                            chatMessage = state.chatMessage.mergeWithChatMessages(messages, state.userId)
                        )
                    }
                }.onFailure {
                    Timber.e(it)
                }
            }
            previousState = current
        }
    }

    private fun getUserInfo() = intent {
        getUserStatusUseCase().catch {
            Timber.e(it)
        }.collectLatest {
            when (it) {
                is User.Student -> {
                    reduce {
                        state.copy(
                            userNickName = it.nickname ?: it.anonymousNickname ?: ""
                        )
                    }
                }

                is User.General -> {
                    reduce {
                        state.copy(
                            userNickName = it.nickname ?: it.anonymousNickname ?: ""
                        )
                    }
                }

                is User.Anonymous -> throw IllegalAccessException()
            }
        }
    }

    private fun getChatRoom(
        articleId: Int,
        chatRoomId: Int?
    ) = intent {
        reduce { state.copy(isLoading = true) }
        getChatRoomUseCase(articleId, chatRoomId).onSuccess { data ->
            reduce {
                state.copy(
                    articleId = data.articleId,
                    chatRoomId = data.chatRoomId,
                    userId = data.userId,
                    articleTitle = data.articleTitle,
                    chatPartnerProfileImage = (data.chatPartnerProfileImage ?: "").toUri(),
                    isLoading = false
                )
            }
            getChatMessages(data.articleId, data.chatRoomId)
            _connectChannel.send(true)
        }.onFailure { e ->
            when (e) {
                is KoinChatException.BlockedException -> {
                    reduce { state.copy(isBlocked = true) }
                    postSideEffect(ChatRoomSideEffect.BlockedByUser)
                }

                else -> Timber.e(e)
            }
        }
    }

    fun connectToWS() = intent {
        chatWSConnectUseCase().onSuccess {
            subscribeChatRoom(state.articleId, state.chatRoomId)
        }.onFailure { error ->
            Timber.e(error)
            postSideEffect(ChatRoomSideEffect.FailedToConnectWS)
        }
    }

    private fun subscribeChatRoom(
        articleId: Int,
        chatRoomId: Int
    ) = intent {
        subscribeChatRoomUseCase(articleId, chatRoomId).catch {
            if (it is UnknownHostException) {
                // Android OS disconnect network when the device enter idle.
                // So, we set shouldReconnect to true
                // And reconnect websocket when activity is resumed
                _connectChannel.send(true)
            } else if (it is WebSocketConnectionException) {
                if (it.cause is UnknownHostException) {
                    // Android OS disconnect network when the device enter idle.
                    // So, we set shouldReconnect to true
                    // And reconnect websocket when activity is resumed
                    _connectChannel.send(true)
                } else {
                    Timber.e(it)
                }
            } else {
                Timber.e(it)
            }
        }.collect { message ->
            reduce {
                state.copy(
                    chatMessage = state.chatMessage.appendMessage(message, state.userId)
                )
            }
        }
    }

    private fun getChatMessages(
        articleId: Int,
        chatRoomId: Int
    ) = intent {
        reduce { state.copy(isLoading = true) }
        getChatMessageUseCase(articleId, chatRoomId).onSuccess { messages ->
            reduce {
                state.copy(
                    isLoading = false,
                    chatMessage = messages.mapToConvertedChatMessages(state.userId)
                )
            }
        }.onFailure {
            Timber.e(it)
        }
    }

    private suspend fun disconnectWS() {
        // We need to disconnect websocket on onCleared. So, we need to use coroutineScope instead of viewModelScope
        try {
            withTimeout(DISCONNECT_TIMEOUT_MS) {
                chatWSDisconnectUseCase().onFailure {
                    // Sometimes the server closes the connection too quickly to send a RECEIPT, which is not really an error
                    // So, we can ignore LostReceiptException
                    // http://stomp.github.io/stomp-specification-1.2.html#Connection_Lingering
                    if (it !is LostReceiptException) {
                        Timber.e(it)
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            Timber.w("WebSocket disconnect timed out after ${DISCONNECT_TIMEOUT_MS}ms, continuing cleanup")
        }
    }

    fun onChatInputValueChange(value: String) = blockingIntent {
        reduce {
            state.copy(chatInputValue = value)
        }
    }

    private fun uploadImage(
        preSignedUrl: String,
        fileUrl: String,
        mediaType: String,
        mediaSize: Long,
        uploadingImage: ConvertedChatMessage
    ) = intent {
        uploadFilesUseCase(
            preSignedUrl,
            mediaType,
            mediaSize,
            uploadingImage.content
        ).onSuccess {
            sendMessageUseCase(
                state.articleId,
                state.chatRoomId,
                ChatMessage(
                    userId = state.userId,
                    userNickname = state.userNickName,
                    content = fileUrl,
                    timestamp = LocalDateTime.now().toString(),
                    isImage = true
                )
            ).onSuccess {
                reduce {
                    state.copy(
                        uploadingImage = state.uploadingImage.filterNot { it.uploadId == uploadingImage.uploadId }
                    )
                }
            }.onFailure {
                Timber.e(it)
                postSideEffect(ChatRoomSideEffect.FailedToSendMessage)
            }
        }.onFailure {
            postSideEffect(ChatRoomSideEffect.FailedToUploadImage)
        }
    }

    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri
    ) = intent {
        val uploadingImage = ConvertedChatMessage(
            userId = state.userId,
            userNickname = state.userNickName,
            content = imageUri.toString(),
            timestamp = LocalDateTime.now(),
            isImage = true,
            isSentByMe = true,
            uploadId = UUID.randomUUID().toString()
        )
        reduce {
            state.copy(
                uploadingImage = state.uploadingImage.plus(uploadingImage)
            )
        }
        getLostAndFoundPreSignedUrlUseCase(
            fileSize,
            fileType,
            fileName
        ).onSuccess {
            uploadImage(
                preSignedUrl = it.second,
                fileUrl = it.first,
                mediaType = fileType,
                mediaSize = fileSize,
                uploadingImage = uploadingImage
            )
        }.onFailure {
            intent {
                postSideEffect(ChatRoomSideEffect.FailedToUploadImage)
            }
        }
    }

    fun sendMessage() = intent {
        if (state.chatInputValue.isBlank()) return@intent
        sendMessageUseCase(
            state.articleId,
            state.chatRoomId,
            ChatMessage(
                userId = state.userId,
                userNickname = state.userNickName,
                content = state.chatInputValue,
                timestamp = LocalDateTime.now().toString(),
                isImage = false
            )
        ).onSuccess {
            reduce {
                state.copy(chatInputValue = "")
            }
        }.onFailure {
            Timber.e(it)
            postSideEffect(ChatRoomSideEffect.FailedToSendMessage)
        }
    }

    fun blockUser() = intent {
        chatBlockUserUseCase(state.articleId, state.chatRoomId).onSuccess {
            postSideEffect(ChatRoomSideEffect.BlockUserSuccess)
        }.onFailure {
            Timber.d(it)
            postSideEffect(ChatRoomSideEffect.BlockUserFailed)
        }
    }

    fun changeMenuState(menuState: Boolean) = intent {
        reduce {
            state.copy(showMenu = menuState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.launch {
            try {
                disconnectWS()
            } finally {
                job.cancel()
            }
        }
    }

    fun changeBlockDialogState(dialogState: Boolean) = intent {
        reduce {
            state.copy(
                showBlockDialog = dialogState,
                showMenu = false // Close menu when dialog state changes
            )
        }
    }

    fun changeShowImageState(
        showImageState: Boolean,
        url: Uri
    ) = intent {
        reduce {
            state.copy(showImage = Pair(showImageState, url))
        }
    }

    companion object {
        const val ARTICLE_ID = "article_id"
        const val CHAT_ROOM_ID = "chat_room_id"
        private const val DISCONNECT_TIMEOUT_MS = 5000L
    }
}
