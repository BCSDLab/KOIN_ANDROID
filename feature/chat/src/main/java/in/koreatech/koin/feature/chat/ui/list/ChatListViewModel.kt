package `in`.koreatech.koin.feature.chat.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.chat.ChatWSConnectUseCase
import `in`.koreatech.koin.domain.usecase.chat.ChatWSDisconnectUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatListUseCase
import `in`.koreatech.koin.domain.usecase.chat.SubscribeChatListUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.LostReceiptException
import org.hildan.krossbow.websocket.WebSocketConnectionException
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getChatListUseCase: GetChatListUseCase,
    private val chatWSConnectUseCase: ChatWSConnectUseCase,
    private val chatWSDisconnectUseCase: ChatWSDisconnectUseCase,
    private val subscribeChatListUseCase: SubscribeChatListUseCase
) : ViewModel(), ContainerHost<ChatListState, ChatListSideEffect> {
    override val container = container<ChatListState, ChatListSideEffect>(ChatListState())

    private val _connectChannel = Channel<Boolean>()
    val connectChannel = _connectChannel.receiveAsFlow()

    init {
        getUserInfo()
    }

    private fun getUserInfo() = intent {
        getUserStatusUseCase().catch {
            Timber.e(it)
        }.collectLatest {
            when (it) {
                is User.Student -> {
                    reduce {
                        state.copy(
                            userId = it.id
                        )
                    }
                }

                is User.General -> {
                    reduce {
                        state.copy(
                            userId = it.id
                        )
                    }
                }

                is User.Anonymous -> throw IllegalAccessException()
            }
            _connectChannel.send(true)
        }
    }

    fun fetchChatList() = viewModelScope.launch {
        getChatListUseCase().collect { data ->
            intent {
                reduce {
                    state.copy(chatList = data, isLoading = false)
                }
            }
        }
    }

    fun connectToWS() = intent {
        if (state.userId == -1) return@intent // If userId is not set, don't connect websocket
        chatWSConnectUseCase().onSuccess {
            subscribeChatList(state.userId)
        }
    }

    fun disconnectWS() = intent { // We need to disconnect websocket on onCleared. So, we need to use coroutineScope instead of viewModelScope
        chatWSDisconnectUseCase().onFailure {
            // Sometimes the server closes the connection too quickly to send a RECEIPT, which is not really an error
            // So, we can ignore LostReceiptException
            // http://stomp.github.io/stomp-specification-1.2.html#Connection_Lingering
            if (it !is LostReceiptException) {
                Timber.e(it)
            }
        }
    }

    private fun subscribeChatList(
        userId: Int
    ) = intent {
        subscribeChatListUseCase(userId).catch {
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
        }.collect { data ->
            reduce {
                state.copy(chatList = data)
            }
        }
    }
}
