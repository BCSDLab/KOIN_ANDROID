package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.response.notification.NotificationPermissionInfoResponse
import javax.inject.Inject

class NotificationRemoteDataSource @Inject constructor(
    private val userAuthApi: UserAuthApi,
) {
    suspend fun getPermissionInfo(): NotificationPermissionInfoResponse =
        userAuthApi.getNotificationPermissionInfo()

    suspend fun updateSubscription(type: String) {
        userAuthApi.updateSubscription(type)
    }

    suspend fun updateSubscriptionDetail(type: String) {
        userAuthApi.updateSubscriptionDetail(type)
    }

    suspend fun deleteSubscription(type: String) {
        userAuthApi.deleteSubscription(type)
    }

    suspend fun deleteSubscriptionDetail(type: String) {
        userAuthApi.deleteSubscriptionDetail(type)
    }
}