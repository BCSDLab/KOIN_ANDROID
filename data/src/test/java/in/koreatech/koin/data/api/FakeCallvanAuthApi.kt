package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.api.auth.CallvanAuthApi
import `in`.koreatech.koin.data.request.callvan.CallvanChatMessageRequest
import `in`.koreatech.koin.data.request.callvan.CallvanPostCreateRequest
import `in`.koreatech.koin.data.request.callvan.CallvanUserReportCreateRequest
import `in`.koreatech.koin.data.response.callvan.CallvanChatMessageResponse
import `in`.koreatech.koin.data.response.callvan.CallvanNotificationResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostCreateResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostDetailResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostSearchResponse

class FakeCallvanAuthApi : CallvanAuthApi {

    var reportException: Throwable? = null
    var capturedRequest: CallvanUserReportCreateRequest? = null

    override suspend fun reportCallvanUser(
        postId: Int,
        callvanUserReportCreateRequest: CallvanUserReportCreateRequest
    ) {
        capturedRequest = callvanUserReportCreateRequest
        reportException?.let { throw it }
    }

    override suspend fun createCallvanPost(
        callvanPostCreateRequest: CallvanPostCreateRequest
    ): CallvanPostCreateResponse = throw NotImplementedError()

    override suspend fun getCallvanPosts(
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
    ): CallvanPostSearchResponse = throw NotImplementedError()

    override suspend fun sendMessage(
        postId: Int,
        callvanChatMessageRequest: CallvanChatMessageRequest
    ) = throw NotImplementedError()

    override suspend fun getCallvanChatMessages(postId: Int): CallvanChatMessageResponse =
        throw NotImplementedError()

    override suspend fun getCallvanPostDetail(postId: Int): CallvanPostDetailResponse =
        throw NotImplementedError()

    override suspend fun closeCallvanPost(postId: Int) = throw NotImplementedError()

    override suspend fun getNotifications(): List<CallvanNotificationResponse> =
        throw NotImplementedError()

    override suspend fun deleteAllNotifications() = throw NotImplementedError()

    override suspend fun deleteNotification(notificationId: Int) = throw NotImplementedError()

    override suspend fun markNotificationAsRead(notificationId: Int) = throw NotImplementedError()

    override suspend fun markAllNotificationsAsRead() = throw NotImplementedError()

    override suspend fun completeCallvanPost(postId: Int) = throw NotImplementedError()

    override suspend fun joinCallvanPost(postId: Int) = throw NotImplementedError()

    override suspend fun leaveCallvanPost(postId: Int) = throw NotImplementedError()

    override suspend fun reopenCallvanPost(postId: Int) = throw NotImplementedError()
}
