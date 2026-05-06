package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.CallvanAuthApi
import `in`.koreatech.koin.data.request.callvan.CallvanChatMessageRequest
import `in`.koreatech.koin.data.request.callvan.CallvanPostCreateRequest
import `in`.koreatech.koin.data.request.callvan.CallvanUserReportCreateRequest
import javax.inject.Inject
import retrofit2.HttpException

class CallvanRemoteDataSource @Inject constructor(
    private val callvanAuthApi: CallvanAuthApi
) {
    suspend fun createCallvanPost(
        callvanPostCreateRequest: CallvanPostCreateRequest
    ) = callvanAuthApi.createCallvanPost(
        callvanPostCreateRequest = callvanPostCreateRequest
    )

    suspend fun getCallvanPosts(
        author: String?,
        departures: List<String>?,
        departureKeyword: String?,
        arrivals: List<String>?,
        arrivalKeyword: String?,
        statuses: List<String>?,
        title: String?,
        sort: String?,
        joined: Boolean,
        page: Int?,
        limit: Int?
    ) = callvanAuthApi.getCallvanPosts(
        author = author,
        departures = departures,
        departureKeyword = departureKeyword,
        arrivals = arrivals,
        arrivalKeyword = arrivalKeyword,
        statuses = statuses,
        title = title,
        sort = sort,
        joined = joined,
        page = page,
        limit = limit
    )

    suspend fun sendMessage(
        postId: Int,
        callvanChatMessageRequest: CallvanChatMessageRequest
    ) = callvanAuthApi.sendMessage(
        postId = postId,
        callvanChatMessageRequest = callvanChatMessageRequest
    )

    suspend fun getCallvanChatMessages(
        postId: Int
    ) = callvanAuthApi.getCallvanChatMessages(
        postId = postId
    )

    suspend fun getCallvanPostDetail(
        postId: Int
    ) = callvanAuthApi.getCallvanPostDetail(
        postId = postId
    )

    suspend fun reportCallvanUser(
        postId: Int,
        callvanUserReportCreateRequest: CallvanUserReportCreateRequest
    ) = callvanAuthApi.reportCallvanUser(
        postId = postId,
        callvanUserReportCreateRequest = callvanUserReportCreateRequest
    )

    suspend fun closeCallvanPost(
        postId: Int
    ) {
        val response = callvanAuthApi.closeCallvanPost(postId = postId)
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun getNotifications() = callvanAuthApi.getNotifications()

    suspend fun deleteAllNotifications() {
        val response = callvanAuthApi.deleteAllNotifications()
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun deleteNotification(
        notificationId: Int
    ) {
        val response = callvanAuthApi.deleteNotification(notificationId = notificationId)
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun markNotificationAsRead(
        notificationId: Int
    ) {
        val response = callvanAuthApi.markNotificationAsRead(notificationId = notificationId)
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun markAllNotificationsAsRead() {
        val response = callvanAuthApi.markAllNotificationsAsRead()
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun completeCallvanPost(
        postId: Int
    ) {
        val response = callvanAuthApi.completeCallvanPost(postId = postId)
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun joinCallvanPost(
        postId: Int
    ) = callvanAuthApi.joinCallvanPost(
        postId = postId
    )

    suspend fun leaveCallvanPost(
        postId: Int
    ) {
        val response = callvanAuthApi.leaveCallvanPost(postId = postId)
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun reopenCallvanPost(
        postId: Int
    ) {
        val response = callvanAuthApi.reopenCallvanPost(postId = postId)
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun getCallvanRestriction() = callvanAuthApi.getCallvanRestriction()
}
