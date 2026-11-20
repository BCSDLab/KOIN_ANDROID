package `in`.koreatech.koin.feature.notification.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import `in`.koreatech.koin.domain.model.notification.Notification
import `in`.koreatech.koin.feature.notification.R
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class LocalNotification(
    val id: Int,
    val type: String,
    val datetimeDiff: ElapsedTime,
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

private fun LocalDateTime.toDatetimeDiff(): ElapsedTime {
    val now = LocalDateTime.now()
    val days = ChronoUnit.DAYS.between(this, now)
    if (days > 0) return ElapsedTime.Days(days)
    val hours = ChronoUnit.HOURS.between(this, now)
    if (hours > 0) return ElapsedTime.Hours(hours)
    val minutes = ChronoUnit.MINUTES.between(this, now)
    if (minutes > 0) return ElapsedTime.Minutes(minutes)
    return ElapsedTime.JustNow
}

sealed interface ElapsedTime {
    data class Days(val value: Long) : ElapsedTime
    data class Hours(val value: Long) : ElapsedTime
    data class Minutes(val value: Long) : ElapsedTime
    data object JustNow : ElapsedTime
}

@Composable
fun ElapsedTime.asString(): String = when (this) {
    is ElapsedTime.Days -> stringResource(R.string.notification_elapsed_days, value)
    is ElapsedTime.Hours -> stringResource(R.string.notification_elapsed_hours, value)
    is ElapsedTime.Minutes -> stringResource(R.string.notification_elapsed_minutes, value)
    ElapsedTime.JustNow -> stringResource(R.string.notification_elapsed_just_now)
}
