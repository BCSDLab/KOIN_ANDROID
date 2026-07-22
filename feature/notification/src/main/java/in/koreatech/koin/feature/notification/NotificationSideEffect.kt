package `in`.koreatech.koin.feature.notification

sealed class NotificationSideEffect {
    data object Error : NotificationSideEffect()
    data class NavigateTo(val url: String) : NotificationSideEffect()
    data object Deleted : NotificationSideEffect()
    data object NewNotificationReceived : NotificationSideEffect()
}
