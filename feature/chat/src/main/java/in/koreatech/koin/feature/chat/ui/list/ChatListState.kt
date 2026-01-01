package `in`.koreatech.koin.feature.chat.ui.list

import `in`.koreatech.koin.domain.model.chat.ChatListItem

data class ChatListState(
    val isLoading: Boolean = true,
    val chatList: List<ChatListItem> = emptyList(),
    val userId: Int = -1
)
