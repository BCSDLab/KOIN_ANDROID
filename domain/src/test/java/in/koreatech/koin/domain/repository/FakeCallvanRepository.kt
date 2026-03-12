package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.callvan.CallvanChatMessage
import `in`.koreatech.koin.domain.model.callvan.CallvanNotification
import `in`.koreatech.koin.domain.model.callvan.CallvanPostCreate
import `in`.koreatech.koin.domain.model.callvan.CallvanPostDetail
import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch

class FakeCallvanRepository : CallvanRepository {

    var reportResult: Result<Unit> = Result.failure(NotImplementedError())

    var capturedPostId: Int? = null
    var capturedReportedUserId: Int? = null
    var capturedDescription: String? = null
    var capturedReasons: List<Pair<String, String?>>? = null
    var capturedAttachmentUrls: List<String>? = null

    override suspend fun reportCallvanUser(
        postId: Int,
        reportedUserId: Int,
        description: String?,
        reasons: List<Pair<String, String?>>,
        attachmentUrls: List<String>?
    ): Result<Unit> {
        capturedPostId = postId
        capturedReportedUserId = reportedUserId
        capturedDescription = description
        capturedReasons = reasons
        capturedAttachmentUrls = attachmentUrls
        return reportResult
    }

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

    override suspend fun getCallvanPostDetail(postId: Int): Result<CallvanPostDetail> =
        throw NotImplementedError()

    override suspend fun closeCallvanPost(postId: Int): Result<Unit> = throw NotImplementedError()

    override suspend fun getNotifications(): Result<List<CallvanNotification>> =
        throw NotImplementedError()

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
