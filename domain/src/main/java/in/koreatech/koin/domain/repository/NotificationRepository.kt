package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.SubscribesType

interface NotificationRepository {
    suspend fun getPermissionInfo(): NotificationPermissionInfo
    suspend fun updateSubscription(type: SubscribesType)
    suspend fun deleteSubscription(type: SubscribesType)
}
