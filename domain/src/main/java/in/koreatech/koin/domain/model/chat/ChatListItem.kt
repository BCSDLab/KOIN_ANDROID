package `in`.koreatech.koin.domain.model.chat

data class ChatListItem(
    val title: String,
    val recentMessage: String,
    val imageUrl: String?,
    val unReadMessageCount: Int,
    val lastMessageAt: String,
    val articleId: Int,
    val chatRoomId: Int,
)
