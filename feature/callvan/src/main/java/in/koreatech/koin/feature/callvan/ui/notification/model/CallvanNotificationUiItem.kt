package `in`.koreatech.koin.feature.callvan.ui.notification.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.domain.model.callvan.CallvanNotification
import `in`.koreatech.koin.feature.callvan.R
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class CallvanNotificationUiItem(
    val id: Int,
    @param:StringRes val titleRes: Int,
    val routeInfo: String,
    val maxParticipants: Int,
    val currentParticipants: Int,
    val message: String,
    val isRead: Boolean
)

fun CallvanNotification.toUiItem() = CallvanNotificationUiItem(
    id = id,
    titleRes = mapNotificationTypeToRes(type),
    routeInfo = formatRouteInfo(this),
    maxParticipants = maxParticipants,
    currentParticipants = currentParticipants,
    message = messagePreview.orEmpty(),
    isRead = isRead
)

private fun mapNotificationTypeToRes(type: String): Int = when (type) {
    CallvanNotificationType.RECRUITMENT_COMPLETE -> R.string.callvan_notification_title_recruitment_complete
    CallvanNotificationType.MEMBER_JOINED -> R.string.callvan_notification_title_member_joined
    CallvanNotificationType.NEW_MESSAGE -> R.string.callvan_notification_title_new_message
    CallvanNotificationType.DEPARTURE_IMMINENT -> R.string.callvan_notification_title_departure_imminent
    else -> R.string.callvan_notification_title_recruitment_complete
}

private fun formatRouteInfo(notification: CallvanNotification): String {
    val date = LocalDate.parse(notification.departureDate)
    val month = date.monthValue.toString().padStart(2, '0')
    val day = date.dayOfMonth.toString().padStart(2, '0')
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)
    return "$month.$day($dayOfWeek) ${notification.departureTime} ${notification.departure} - ${notification.arrival}"
}
