package `in`.koreatech.koin.feature.notification.model

import `in`.koreatech.koin.domain.model.notification.Notification
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class LocalNotification(
    val id: Int,
    val type: String,
    val datetimeDiff: String,
    val title: String,
    val content: String,
    val originUrl: String,
    val isRead: Boolean
)

fun Notification.toLocalNotification(): LocalNotification = LocalNotification(
    id = id,
    type = type,
    datetimeDiff = datetime.toDatetimeDiff(),
    title = title,
    content = content,
    originUrl = originUrl,
    isRead = isRead
)

private fun LocalDateTime.toDatetimeDiff(): String {
    val now = LocalDateTime.now()
    val days = ChronoUnit.DAYS.between(this, now)
    if (days > 0) return "${days}일 전"
    val hours = ChronoUnit.HOURS.between(this, now)
    if (hours > 0) return "${hours}시간 전"
    val minutes = ChronoUnit.MINUTES.between(this, now)
    if (minutes > 0) return "${minutes}분 전"
    return "방금 전"
}
