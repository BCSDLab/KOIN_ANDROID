package `in`.koreatech.koin.feature.chat.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.chat.GetChatListUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel
    @Inject
    constructor(
        private val getChatListUseCase: GetChatListUseCase,
    ) : ViewModel(), ContainerHost<ChatListState, ChatListSideEffect> {
        override val container = container<ChatListState, ChatListSideEffect>(ChatListState())

        fun fetchChatList() =
            viewModelScope.launch {
                getChatListUseCase().collect { data ->
                    intent {
                        reduce {
                            state.copy(chatList = data)
                        }
                    }
                }
            }
    }
