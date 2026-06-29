package `in`.koreatech.koin.feature.notification

import `in`.koreatech.koin.feature.notification.model.LocalNotification
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NotificationState(
    val isLoading: Boolean = false,
    val notifications: ImmutableList<LocalNotification> = persistentListOf()
)
