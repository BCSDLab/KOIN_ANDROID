package `in`.koreatech.koin.feature.callvan.ui.detail

import `in`.koreatech.koin.domain.model.callvan.CallvanChatMessage
import `in`.koreatech.koin.domain.model.callvan.CallvanNotification
import `in`.koreatech.koin.domain.model.callvan.CallvanPostCreate
import `in`.koreatech.koin.domain.model.callvan.CallvanPostDetail
import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch
import `in`.koreatech.koin.domain.repository.CallvanRepository

class FakeCallvanRepository : CallvanRepository {

    var postDetailResult: Result<CallvanPostDetail> =
        Result.failure(NotImplementedError())
    var notificationsResult: Result<List<CallvanNotification>> =
        Result.failure(NotImplementedError())

    override suspend fun getCallvanPostDetail(postId: Int) = postDetailResult

    override suspend fun getNotifications() = notificationsResult

    override suspend fun createCallvanPost(
        departureType: String,
        departureCustomName: String?,
        arrivalType: String,
        arrivalCustomName: String?,
        departureDate: String,
        departureTime: String,
        maxParticipants: Int
    ): Result<CallvanPostCreate> = throw NotImplementedError()

    override suspend fun getCallvanPosts(
        author: String?,
        departures: List<String>?,
        departureKeyword: String?,
        arrivals: List<String>?,
        arrivalKeyword: String?,
        statuses: List<String>?,
        title: String?,
        sort: String?,
        page: Int?,
        limit: Int?
    ): Result<CallvanPostSearch> = throw NotImplementedError()

    override suspend fun sendMessage(postId: Int, isImage: Boolean, content: String): Result<Unit> =
        throw NotImplementedError()

    override suspend fun getCallvanChatMessages(postId: Int): Result<CallvanChatMessage> =
        throw NotImplementedError()

    override suspend fun reportCallvanUser(
        postId: Int,
        reportedUserId: Int,
        reasons: List<Pair<String, String?>>
    ): Result<Unit> = throw NotImplementedError()

    override suspend fun closeCallvanPost(postId: Int): Result<Unit> = throw NotImplementedError()

    override suspend fun deleteAllNotifications(): Result<Unit> = throw NotImplementedError()

    override suspend fun deleteNotification(notificationId: Int): Result<Unit> =
        throw NotImplementedError()

    override suspend fun markNotificationAsRead(notificationId: Int): Result<Unit> =
        throw NotImplementedError()

    override suspend fun markAllNotificationsAsRead(): Result<Unit> = throw NotImplementedError()

    override suspend fun completeCallvanPost(postId: Int): Result<Unit> = throw NotImplementedError()

    override suspend fun joinCallvanPost(postId: Int): Result<Unit> = throw NotImplementedError()

    override suspend fun leaveCallvanPost(postId: Int): Result<Unit> = throw NotImplementedError()

    override suspend fun reopenCallvanPost(postId: Int): Result<Unit> = throw NotImplementedError()
}