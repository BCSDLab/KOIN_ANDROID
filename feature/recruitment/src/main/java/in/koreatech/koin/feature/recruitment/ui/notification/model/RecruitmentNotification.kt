package `in`.koreatech.koin.feature.recruitment.ui.notification.model

import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotification as DomainRecruitmentNotification
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

internal data class RecruitmentNotification(
    val id: Int,
    val recruitmentId: Int,
    val targetType: String,
    val category: RecruitmentNotificationCategory,
    val senderNickname: String?,
    val content: String,
    val timestamp: String,
    val isRead: Boolean
)

internal fun DomainRecruitmentNotification.toUiModel(): RecruitmentNotification {
    val category = type.toRecruitmentNotificationCategory()
    return RecruitmentNotification(
        id = id,
        recruitmentId = recruitmentId,
        targetType = targetType,
        category = category,
        senderNickname = senderNickname,
        content = messagePreview,
        timestamp = createdAt.toDatetimeDiff(),
        isRead = isRead
    )
}

private fun String.toRecruitmentNotificationCategory(): RecruitmentNotificationCategory = when (this) {
    "NEW_APPLICATION" -> RecruitmentNotificationCategory.NEW_APPLICATION
    "APPLICATION_ACCEPTED" -> RecruitmentNotificationCategory.APPLICATION_APPROVED
    "APPLICATION_REJECTED" -> RecruitmentNotificationCategory.APPLICATION_REJECTED
    "RECRUITMENT_CLOSED" -> RecruitmentNotificationCategory.RECRUITMENT_CLOSED
    "RECRUITMENT_DELETED" -> RecruitmentNotificationCategory.POST_DELETED
    "NEW_CHAT_MESSAGE" -> RecruitmentNotificationCategory.MESSAGE
    else -> RecruitmentNotificationCategory.MESSAGE
}

private val CREATED_AT_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

private fun String.toDatetimeDiff(): String {
    val createdAt = LocalDateTime.parse(this, CREATED_AT_FORMATTER)
    val now = LocalDateTime.now()
    val days = ChronoUnit.DAYS.between(createdAt, now)
    if (days > 0) return "${days}일 전"
    val hours = ChronoUnit.HOURS.between(createdAt, now)
    if (hours > 0) return "${hours}시간 전"
    val minutes = ChronoUnit.MINUTES.between(createdAt, now)
    if (minutes > 0) return "${minutes}분 전"
    return "방금 전"
}
