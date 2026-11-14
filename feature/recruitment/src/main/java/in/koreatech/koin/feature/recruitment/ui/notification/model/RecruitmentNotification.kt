package `in`.koreatech.koin.feature.recruitment.ui.notification.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotification as DomainRecruitmentNotification
import `in`.koreatech.koin.feature.recruitment.R
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
    val timestamp: ElapsedTime,
    val isRead: Boolean,
    val applicationId: Int?,
    val chatRoomId: Int?
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
        isRead = isRead,
        applicationId = applicationId,
        chatRoomId = chatRoomId
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

private val CREATED_AT_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

private fun String.toDatetimeDiff(): ElapsedTime {
    val createdAt = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val now = LocalDateTime.now()
    val days = ChronoUnit.DAYS.between(createdAt, now)
    if (days > 0) return ElapsedTime.Days(days)
    val hours = ChronoUnit.HOURS.between(createdAt, now)
    if (hours > 0) return ElapsedTime.Hours(hours)
    val minutes = ChronoUnit.MINUTES.between(createdAt, now)
    if (minutes > 0) return ElapsedTime.Minutes(minutes)
    return ElapsedTime.JustNow
}

internal sealed interface ElapsedTime {
    data class Days(val value: Long) : ElapsedTime
    data class Hours(val value: Long) : ElapsedTime
    data class Minutes(val value: Long) : ElapsedTime
    data object JustNow : ElapsedTime
}

@Composable
internal fun ElapsedTime.asString(): String = when (this) {
    is ElapsedTime.Days -> stringResource(R.string.recruitment_elapsed_days, value)
    is ElapsedTime.Hours -> stringResource(R.string.recruitment_elapsed_hours, value)
    is ElapsedTime.Minutes -> stringResource(R.string.recruitment_elapsed_minutes, value)
    ElapsedTime.JustNow -> stringResource(R.string.recruitment_elapsed_just_now)
}
