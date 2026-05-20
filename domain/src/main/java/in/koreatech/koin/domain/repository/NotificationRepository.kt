package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType

interface NotificationRepository {
    suspend fun getPermissionInfo(): Result<NotificationPermissionInfo>

    suspend fun postReviewPromptNotification(storeId: Int)

    suspend fun updateSubscription(type: SubscribesType): Result<Unit>

    suspend fun updateSubscriptionDetail(type: SubscribesDetailType): Result<Unit>

    suspend fun deleteSubscription(type: SubscribesType): Result<Unit>

    suspend fun deleteSubscriptionDetail(type: SubscribesDetailType): Result<Unit>
}
