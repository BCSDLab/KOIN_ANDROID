package `in`.koreatech.koin.feature.chat.ui.room

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.repository.ChatRepository
import `in`.koreatech.koin.domain.usecase.chat.ChatWSConnectUseCase
import `in`.koreatech.koin.domain.usecase.chat.ChatWSDisconnectUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.LostReceiptException
import org.hildan.krossbow.websocket.reconnection.WebSocketReconnectionException
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class ChatRoomViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val chatWSConnectUseCase: ChatWSConnectUseCase,
    private val chatWSDisconnectUseCase: ChatWSDisconnectUseCase
) : ViewModel(), ContainerHost<ChatRoomState, ChatRoomSideEffect> {
    override val container = container<ChatRoomState, ChatRoomSideEffect>(ChatRoomState())

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): ChatRoomViewModel
    }

    init {
        savedStateHandle.get<Int>(ARTICLE_ID)?.let {
            getChatRoom(it)
        }
    }

    private fun getChatRoom(articleId: Int) = viewModelScope.launch {
        chatRepository.getChatRoomFromArticleId(articleId).collectLatest {
            intent {
                reduce {
                    state.copy(
                        articleId = it.articleId,
                        chatRoomId = it.chatRoomId,
                        userId = it.userId,
                        articleTitle = it.articleTitle,
                        chatPartnerProfileImage = Uri.parse(it.chatPartnerProfileImage)
                    )
                }
            }
            chatWSConnectUseCase().onFailure { error ->
                if (error is WebSocketReconnectionException) {
                    // Handle reconnection error
                    Timber.d("${error.message}")
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

    companion object {
        const val ARTICLE_ID = "article_id"
    }
}
