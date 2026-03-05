package `in`.koreatech.koin.feature.callvan.ui.notification

import `in`.koreatech.koin.feature.callvan.ui.notification.component.CallvanNotificationUiItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CallvanNotificationsState(
    val notifications: ImmutableList<CallvanNotificationUiItem> = persistentListOf(),
    val isLoading: Boolean = false,
)