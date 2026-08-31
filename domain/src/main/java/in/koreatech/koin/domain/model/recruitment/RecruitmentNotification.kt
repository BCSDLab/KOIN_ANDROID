package `in`.koreatech.koin.domain.model.recruitment

data class RecruitmentNotification(
    val id: Int,
    val type: String,
    val targetType: String,
    val messagePreview: String,
    val senderNickname: String?,
    val isRead: Boolean,
    val createdAt: String,
    val recruitmentId: Int,
    val applicationId: Int?,
    val chatRoomId: Int?
)
