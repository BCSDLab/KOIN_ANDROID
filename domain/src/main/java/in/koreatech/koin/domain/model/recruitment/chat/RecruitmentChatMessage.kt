package `in`.koreatech.koin.domain.model.recruitment.chat

import java.time.LocalDateTime

data class RecruitmentChatMessage(
    val messageId: Int,
    val userId: Int,
    val userNickname: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isImage: Boolean,
    val unreadCount: Int
)
