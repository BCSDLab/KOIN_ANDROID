package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.notification.Notification
import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType

interface NotificationRepository {
    suspend fun getPermissionInfo(): Result<NotificationPermissionInfo>

    suspend fun postReviewPromptNotification(storeId: Int): Result<Unit>

    suspend fun updateSubscription(type: SubscribesType): Result<Unit>

    suspend fun updateSubscriptionDetail(type: SubscribesDetailType): Result<Unit>

    suspend fun deleteSubscription(type: SubscribesType): Result<Unit>

    suspend fun deleteSubscriptionDetail(type: SubscribesDetailType): Result<Unit>

    suspend fun insertNotificationToLocal(notification: Notification): Result<Unit>

    suspend fun insertNotificationsToLocal(notifications: List<Notification>): Result<Unit>

    suspend fun getNotificationsFromLocal(): Result<List<Notification>>

    suspend fun deleteNotificationFromLocal(id: Int): Result<Unit>

    suspend fun deleteNotificationsFromLocal(ids: List<Int>): Result<Unit>
}
