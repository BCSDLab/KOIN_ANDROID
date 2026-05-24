package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toNotificationPermissionInfo
import `in`.koreatech.koin.data.source.remote.NotificationRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.data.util.suspendRunCatching
import `in`.koreatech.koin.domain.error.notification.KoinNotificationException
import `in`.koreatech.koin.domain.error.store.KoinStoreException
import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject
import retrofit2.HttpException

class NotificationRepositoryImpl @Inject constructor(
    private val notificationRemoteDataSource: NotificationRemoteDataSource
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
}
