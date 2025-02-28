package `in`.koreatech.koin.domain.model.chat

data class ChatMessage(
    val userId: Int,
    val userNickname: String,
    val content: String,
    val timestamp: String,
    val isImage: Boolean
)
