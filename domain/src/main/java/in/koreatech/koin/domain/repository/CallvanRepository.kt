package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.callvan.CallvanChatMessage
import `in`.koreatech.koin.domain.model.callvan.CallvanNotification
import `in`.koreatech.koin.domain.model.callvan.CallvanPostCreate
import `in`.koreatech.koin.domain.model.callvan.CallvanPostDetail
import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch

interface CallvanRepository {
    suspend fun createCallvanPost(
        departureType: String,
        departureCustomName: String?,
        arrivalType: String,
        arrivalCustomName: String?,
        departureDate: String,
        departureTime: String,
        maxParticipants: Int
    ): Result<CallvanPostCreate>

    suspend fun getCallvanPosts(
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
    ): Result<CallvanPostSearch>

    suspend fun sendMessage(
        postId: Int,
        isImage: Boolean,
        content: String
    ): Result<Unit>

    suspend fun getCallvanChatMessages(
        postId: Int
    ): Result<CallvanChatMessage>

    suspend fun getCallvanPostDetail(
        postId: Int
    ): Result<CallvanPostDetail>

    suspend fun reportCallvanUser(
        postId: Int,
        reportedUserId: Int,
        reasons: List<Pair<String, String?>>
    ): Result<Unit>

    suspend fun closeCallvanPost(
        postId: Int
    ): Result<Unit>

    suspend fun getNotifications(): Result<List<CallvanNotification>>

    suspend fun deleteAllNotifications(): Result<Unit>

    suspend fun deleteNotification(
        notificationId: Int
    ): Result<Unit>

    suspend fun markNotificationAsRead(
        notificationId: Int
    ): Result<Unit>

    suspend fun markAllNotificationsAsRead(): Result<Unit>

    suspend fun completeCallvanPost(
        postId: Int
    ): Result<Unit>

    suspend fun joinCallvanPost(
        postId: Int
    ): Result<Unit>

    suspend fun leaveCallvanPost(
        postId: Int
    ): Result<Unit>

    suspend fun reopenCallvanPost(
        postId: Int
    ): Result<Unit>
}
