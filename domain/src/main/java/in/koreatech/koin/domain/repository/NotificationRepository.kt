package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType

interface NotificationRepository {
    suspend fun getPermissionInfo(): NotificationPermissionInfo
    suspend fun updateSubscription(type: SubscribesType)
    suspend fun updateSubscriptionDetail(type: SubscribesDetailType)
    suspend fun deleteSubscription(type: SubscribesType)
    suspend fun deleteSubscriptionDetail(type: SubscribesDetailType)
}
