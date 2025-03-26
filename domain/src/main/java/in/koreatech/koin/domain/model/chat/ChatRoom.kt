package `in`.koreatech.koin.domain.model.chat

data class ChatRoom(
    val articleId: Int,
    val chatRoomId: Int,
    val userId: Int,
    val articleTitle: String,
    val chatPartnerProfileImage: String?
)
