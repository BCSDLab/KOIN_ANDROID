package `in`.koreatech.koin.feature.chat.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.chat.SubscribeLostItemChatListUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.chat.ui.mapper.toChatListItems
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val subscribeChatListUseCase: SubscribeLostItemChatListUseCase
) : ViewModel(), ContainerHost<ChatListState, ChatListSideEffect> {
    override val container = container<ChatListState, ChatListSideEffect>(ChatListState())

    private var isChatListVisible = false
    private var chatListUpdateJob: Job? = null

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
                        state.copy(userId = it.id)
                    }
                    startChatListUpdatesIfVisible()
                }

                is User.General -> {
                    reduce {
                        state.copy(userId = it.id)
                    }
                    startChatListUpdatesIfVisible()
                }

                is User.Anonymous -> {
                    reduce {
                        state.copy(userId = -1)
                    }
                    chatListUpdateJob?.cancel()
                    chatListUpdateJob = null
                    postSideEffect(ChatListSideEffect.NavigateToLogin)
                }
            }
        }
    }

    fun startChatListUpdates() {
        isChatListVisible = true
        startChatListUpdatesIfVisible()
    }

    fun stopChatListUpdates() {
        isChatListVisible = false
        chatListUpdateJob?.cancel()
        chatListUpdateJob = null
    }

    private fun startChatListUpdatesIfVisible() {
        if (!isChatListVisible || container.stateFlow.value.userId == -1 || chatListUpdateJob?.isActive == true) return

        chatListUpdateJob = viewModelScope.launch {
            subscribeChatListUseCase().map { it.toChatListItems() }.catch {
                Timber.e(it)
            }.collect { data ->
                intent {
                    reduce {
                        state.copy(chatList = data, isLoading = false)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        stopChatListUpdates()
        super.onCleared()
    }
}
