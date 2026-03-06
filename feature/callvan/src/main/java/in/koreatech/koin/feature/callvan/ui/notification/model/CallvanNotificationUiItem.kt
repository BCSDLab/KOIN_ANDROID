package `in`.koreatech.koin.feature.callvan.ui.notification.model

data class CallvanNotificationUiItem(
    val id: Int,
    val title: String,
    val routeInfo: String,
    val message: String,
    val isRead: Boolean
)
