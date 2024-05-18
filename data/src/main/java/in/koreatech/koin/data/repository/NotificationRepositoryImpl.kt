package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toNotificationPermissionInfo
import `in`.koreatech.koin.data.source.remote.NotificationRemoteDataSource
import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
) : NotificationRepository {
    override suspend fun getPermissionInfo(): NotificationPermissionInfo {
        return notificationRemoteDataSource.getPermissionInfo().toNotificationPermissionInfo()
    }

    override suspend fun updateSubscription(type: SubscribesType) {
        notificationRemoteDataSource.updateSubscription(type.toString())
    }

    override suspend fun updateSubscriptionDetail(type: SubscribesDetailType) {
        notificationRemoteDataSource.updateSubscriptionDetail(type.toString())
    }

    override suspend fun deleteSubscription(type: SubscribesType) {
        notificationRemoteDataSource.deleteSubscription(type.toString())
    }

    override suspend fun deleteSubscriptionDetail(type: SubscribesDetailType) {
        notificationRemoteDataSource.deleteSubscriptionDetail(type.toString())
    }
}