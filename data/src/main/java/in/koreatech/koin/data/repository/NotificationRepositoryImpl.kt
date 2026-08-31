package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toNotification
import `in`.koreatech.koin.data.mapper.toNotificationEntity
import `in`.koreatech.koin.data.mapper.toNotificationPermissionInfo
import `in`.koreatech.koin.data.source.local.NotificationLocalDataSource
import `in`.koreatech.koin.data.source.remote.NotificationRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.notification.KoinNotificationException
import `in`.koreatech.koin.domain.error.store.KoinStoreException
import `in`.koreatech.koin.domain.model.notification.Notification
import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.repository.NotificationRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlin.collections.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

@Suppress("Detekt.TooManyFunctions")
class NotificationRepositoryImpl @Inject constructor(
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
    private val notificationLocalDataSource: NotificationLocalDataSource
) : NotificationRepository {
    override suspend fun getPermissionInfo(): Result<NotificationPermissionInfo> {
        return suspendRunCatching {
            notificationRemoteDataSource.getPermissionInfo().toNotificationPermissionInfo()
        }.mapHttpFailure {
            on(400) throws KoinNotificationException.BadRequestException()
            on(401) throws KoinNotificationException.UnauthorizedException()
            on(403) throws KoinNotificationException.ForbiddenException()
            on(404) throws KoinNotificationException.NotFoundException()
        }
    }

    override suspend fun postReviewPromptNotification(storeId: Int): Result<Unit> {
        return suspendRunCatching {
            notificationRemoteDataSource.postReviewPromptNotification(storeId)
        }.mapHttpFailure {
            on(401) throws KoinStoreException.UnauthorizedException()
            on(403) throws KoinStoreException.ForbiddenException()
            on(404) throws KoinStoreException.ShopNotFoundException()
        }
    }

    override suspend fun updateSubscription(type: SubscribesType): Result<Unit> {
        return suspendRunCatching {
            notificationRemoteDataSource.updateSubscription(type.toString())
        }.mapHttpFailure {
            on(400) throws KoinNotificationException.BadRequestException()
            on(401) throws KoinNotificationException.UnauthorizedException()
            on(403) throws KoinNotificationException.ForbiddenException()
            on(404) throws KoinNotificationException.NotFoundException()
        }
    }

    override suspend fun updateSubscriptionDetail(type: SubscribesDetailType): Result<Unit> {
        return suspendRunCatching {
            notificationRemoteDataSource.updateSubscriptionDetail(type.toString())
        }.mapHttpFailure {
            on(400) throws KoinNotificationException.BadRequestException()
            on(401) throws KoinNotificationException.UnauthorizedException()
            on(403) throws KoinNotificationException.ForbiddenException()
            on(404) throws KoinNotificationException.NotFoundException()
        }
    }

    override suspend fun deleteSubscription(type: SubscribesType): Result<Unit> {
        return suspendRunCatching {
            val response = notificationRemoteDataSource.deleteSubscription(type.toString())
            if (response.isSuccessful) Unit else throw HttpException(response)
        }.mapHttpFailure {
            on(400) throws KoinNotificationException.BadRequestException()
            on(401) throws KoinNotificationException.UnauthorizedException()
            on(403) throws KoinNotificationException.ForbiddenException()
            on(404) throws KoinNotificationException.NotFoundException()
        }
    }

    override suspend fun deleteSubscriptionDetail(type: SubscribesDetailType): Result<Unit> {
        return suspendRunCatching {
            val response = notificationRemoteDataSource.deleteSubscriptionDetail(type.toString())
            if (response.isSuccessful) Unit else throw HttpException(response)
        }.mapHttpFailure {
            on(400) throws KoinNotificationException.BadRequestException()
            on(401) throws KoinNotificationException.UnauthorizedException()
            on(403) throws KoinNotificationException.ForbiddenException()
            on(404) throws KoinNotificationException.NotFoundException()
        }
    }

    override suspend fun insertNotificationToLocal(notification: Notification): Result<Unit> {
        return suspendRunCatching {
            notificationLocalDataSource.insertNotification(notification.toNotificationEntity())
        }
    }

    override suspend fun insertNotificationsToLocal(notifications: List<Notification>): Result<Unit> {
        return suspendRunCatching {
            notificationLocalDataSource.insertNotifications(
                notifications.map { it.toNotificationEntity() }
            )
        }
    }

    override suspend fun updateNotificationReadByUrl(url: String, isRead: Boolean): Result<Notification> {
        return suspendRunCatching {
            notificationLocalDataSource.updateNotificationReadByUrl(url, isRead).toNotification()
        }
    }

    override suspend fun updateNotificationReadById(id: Int, isRead: Boolean): Result<Notification> {
        return suspendRunCatching {
            notificationLocalDataSource.updateNotificationReadById(id, isRead).toNotification()
        }
    }

    override suspend fun getNotificationsFromLocal(): Result<List<Notification>> {
        return suspendRunCatching {
            notificationLocalDataSource.getNotifications().map { it.toNotification() }
        }
    }

    override fun getNotificationsFlowFromLocal(): Flow<List<Notification>> {
        return notificationLocalDataSource.getNotificationsFlow()
            .map { list -> list.map { it.toNotification() } }
    }

    override suspend fun deleteNotificationFromLocal(id: Int): Result<Unit> {
        return suspendRunCatching {
            notificationLocalDataSource.deleteNotification(id)
        }
    }

    override suspend fun deleteNotificationsFromLocal(ids: List<Int>): Result<Unit> {
        return suspendRunCatching {
            notificationLocalDataSource.deleteNotifications(ids)
        }
    }
}
