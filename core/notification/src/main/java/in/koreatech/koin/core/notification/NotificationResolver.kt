package `in`.koreatech.koin.core.notification

interface NotificationResolver {
    fun resolve(payload: NotificationPayload): NotificationType?
}
