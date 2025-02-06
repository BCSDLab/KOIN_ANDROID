package `in`.koreatech.koin.feature.chat.ui.room

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.chat.ChatWSConnectUseCase
import `in`.koreatech.koin.domain.usecase.chat.ChatWSDisconnectUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatMessageUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatRoomUseCase
import `in`.koreatech.koin.domain.usecase.chat.SendMessageUseCase
import `in`.koreatech.koin.domain.usecase.chat.SubscribeChatRoomUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetLostAndFoundPreSignedUrlUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.chat.ui.model.toConvertedChatMessage
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.LostReceiptException
import org.hildan.krossbow.websocket.reconnection.WebSocketReconnectionException
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import java.time.LocalDateTime

@HiltViewModel(assistedFactory = ChatRoomViewModel.Factory::class)
class ChatRoomViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val chatWSConnectUseCase: ChatWSConnectUseCase,
    private val chatWSDisconnectUseCase: ChatWSDisconnectUseCase,
    private val getChatRoomUseCase: GetChatRoomUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val subscribeChatRoomUseCase: SubscribeChatRoomUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getLostAndFoundPreSignedUrlUseCase: GetLostAndFoundPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase
) : ViewModel(), ContainerHost<ChatRoomState, ChatRoomSideEffect> {
    override val container = container<ChatRoomState, ChatRoomSideEffect>(ChatRoomState())

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): ChatRoomViewModel
    }

    init {
        getUserInfo()
        savedStateHandle.get<Int>(ARTICLE_ID)?.let {
            getChatRoom(it)
        }
    }

    private fun getUserInfo() = viewModelScope.launch {
        getUserStatusUseCase().catch {
            Timber.e(it)
        }.collectLatest {
            intent {
                if (it is User.Student) {
                    reduce {
                        state.copy(
                            userId = Integer.parseInt(it.studentNumber ?: ""),
                            userNickName = it.nickname ?: it.anonymousNickname ?: ""
                        )
                    }
                }
            }
        }
    }

    private fun getChatRoom(articleId: Int) = viewModelScope.launch {
        getChatRoomUseCase(articleId).catch {
            Timber.e(it)
        }.collectLatest { data ->
            intent {
                reduce {
                    state.copy(
                        articleId = data.articleId,
                        chatRoomId = data.chatRoomId,
                        userId = data.userId,
                        articleTitle = data.articleTitle,
                        chatPartnerProfileImage = Uri.parse(data.chatPartnerProfileImage ?: "")
                    )
                }
                getChatMessages(data.articleId, data.chatRoomId)
            }
        }
        connectToWS()
    }

    private fun connectToWS() = viewModelScope.launch {
        intent {
            chatWSConnectUseCase().onSuccess {
                subscribeChatRoom(state.articleId, state.chatRoomId)
            }.onFailure { error ->
                if (error is WebSocketReconnectionException) {
                    // Handle reconnection error
                    Timber.d("${error.message}")
                    intent {
                        postSideEffect(ChatRoomSideEffect.FailedToConnectWS)
                    }
                }
            }
        }
    }

    private fun subscribeChatRoom(articleId: Int, chatRoomId: Int) = viewModelScope.launch {
        subscribeChatRoomUseCase(articleId, chatRoomId).catch {
            Timber.e(it)
        }.collect { message ->
            intent {
                if (state.chatMessage.isEmpty()) {
                    reduce {
                        state.copy(
                            chatMessage = listOf(
                                Pair(
                                    LocalDateTime.parse(message.timestamp).toLocalDate(),
                                    listOf(message.toConvertedChatMessage(state.userId))
                                )
                            )
                        )
                    }
                    return@intent
                }
                if (state.chatMessage.last().first < LocalDateTime.parse(message.timestamp)
                        .toLocalDate()
                ) {
                    reduce {
                        state.copy(
                            chatMessage = state.chatMessage.plus(
                                Pair(
                                    LocalDateTime.parse(message.timestamp).toLocalDate(), listOf(
                                        message.toConvertedChatMessage(state.userId)
                                    )
                                )
                            ),
                        )
                    }
                } else {
                    reduce {
                        state.copy(
                            chatMessage = state.chatMessage.dropLast(1).plus(
                                Pair(
                                    state.chatMessage.last().first,
                                    state.chatMessage.last().second.plus(
                                        message.toConvertedChatMessage(state.userId)
                                    )
                                )
                            ),
                        )
                    }
                }
            }
        }
        getChatMessages(articleId, chatRoomId)
    }

    private fun getChatMessages(articleId: Int, chatRoomId: Int) = viewModelScope.launch {
        getChatMessageUseCase(articleId, chatRoomId).catch {
            Timber.e(it)
        }.collect { messages ->
            intent {
                reduce {
                    state.copy(chatMessage = messages.map { it.toConvertedChatMessage(state.userId) }
                        .groupBy { it.timestamp.toLocalDate() }.toList())
                }
            }
        }
    }

    fun disconnectWS() = viewModelScope.launch {
        chatWSDisconnectUseCase().onFailure {
            // Sometimes the server closes the connection too quickly to send a RECEIPT, which is not really an error
            // So, we can ignore LostReceiptException
            // http://stomp.github.io/stomp-specification-1.2.html#Connection_Lingering
            if (it !is LostReceiptException) {
                Timber.e(it)
            }
        }
    }

    fun onChatInputValueChange(value: String) = intent {
        reduce {
            state.copy(chatInputValue = value)
        }
    }

    private fun uploadImage(
        preSignedUrl: String,
        fileUrl: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: Uri,
    ) = viewModelScope.launch {
        uploadFilesUseCase(
            preSignedUrl,
            mediaType,
            mediaSize,
            imageUri.toString()
        ).onSuccess {
            intent {
                sendMessageUseCase(
                    state.articleId,
                    state.chatRoomId,
                    ChatMessage(
                        userId = state.userId,
                        userNickname = state.userNickName,
                        content = fileUrl,
                        timestamp = LocalDateTime.now().toString(),
                        isImage = true,
                        isSentByMe = true
                    )
                )
            }
        }.onFailure {
            intent {
                postSideEffect(ChatRoomSideEffect.FailedToUploadImage)
            }
        }
    }

    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri
    ) = viewModelScope.launch {
        getLostAndFoundPreSignedUrlUseCase(
            fileSize, fileType, fileName
        ).onSuccess {
            uploadImage(
                preSignedUrl = it.second,
                fileUrl = it.first,
                mediaType = fileType,
                mediaSize = fileSize,
                imageUri = imageUri
            )
        }.onFailure {
            intent {
                postSideEffect(ChatRoomSideEffect.FailedToUploadImage)
            }
        }
    }

    fun sendMessage() {
        intent {
            if (state.chatInputValue.isBlank()) return@intent
            viewModelScope.launch {
                sendMessageUseCase(
                    state.articleId,
                    state.chatRoomId,
                    ChatMessage(
                        userId = state.userId,
                        userNickname = state.userNickName,
                        content = state.chatInputValue,
                        timestamp = LocalDateTime.now().toString(),
                        isImage = false,
                        isSentByMe = true
                    )
                )
                reduce {
                    state.copy(chatInputValue = "")
                }
            }

        }
    }

    fun changeMenuState(menuState: Boolean) = intent {
        reduce {
            state.copy(showMenu = menuState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWS()
    }

    companion object {
        const val ARTICLE_ID = "article_id"
    }
}
