package `in`.koreatech.koin.domain.model.recruitment.chat

data class RecruitmentChatMessage(
    val messageId: Int,
    val userId: Int,
    val userNickname: String,
    val content: String,
    val date: String,
    val time: String,
    val isImage: Boolean,
    val unreadCount: Int
)
