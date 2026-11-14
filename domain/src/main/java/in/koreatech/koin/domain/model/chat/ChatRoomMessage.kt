package `in`.koreatech.koin.domain.model.chat

data class ChatRoomMessage(
    val messageId: String,
    val userId: Int?,
    val userNickname: String?,
    val content: String,
    val timestamp: String,
    val isImage: Boolean,
    val unreadCount: Int?,
    val isLeftUser: Boolean
)
