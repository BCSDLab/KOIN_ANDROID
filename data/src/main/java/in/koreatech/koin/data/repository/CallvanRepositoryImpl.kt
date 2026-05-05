package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toCallvanChatMessage
import `in`.koreatech.koin.data.mapper.toCallvanNotification
import `in`.koreatech.koin.data.mapper.toCallvanPostCreate
import `in`.koreatech.koin.data.mapper.toCallvanPostDetail
import `in`.koreatech.koin.data.mapper.toCallvanPostSearch
import `in`.koreatech.koin.data.mapper.toCallvanRestriction
import `in`.koreatech.koin.data.request.callvan.CallvanChatMessageRequest
import `in`.koreatech.koin.data.request.callvan.CallvanPostCreateRequest
import `in`.koreatech.koin.data.request.callvan.CallvanUserReportCreateRequest
import `in`.koreatech.koin.data.source.remote.CallvanRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.callvan.KoinCallvanException
import `in`.koreatech.koin.domain.model.callvan.CallvanChatMessage
import `in`.koreatech.koin.domain.model.callvan.CallvanNotification
import `in`.koreatech.koin.domain.model.callvan.CallvanPostCreate
import `in`.koreatech.koin.domain.model.callvan.CallvanPostDetail
import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch
import `in`.koreatech.koin.domain.model.callvan.CallvanRestriction
import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class CallvanRepositoryImpl @Inject constructor(
    private val callvanRemoteDataSource: CallvanRemoteDataSource
) : CallvanRepository {
    override suspend fun createCallvanPost(
        departureType: String,
        departureCustomName: String?,
        arrivalType: String,
        arrivalCustomName: String?,
        departureDate: String,
        departureTime: String,
        maxParticipants: Int
    ): Result<CallvanPostCreate> {
        return runCatching {
            callvanRemoteDataSource.createCallvanPost(
                CallvanPostCreateRequest(
                    departureType = departureType,
                    departureCustomName = departureCustomName,
                    arrivalType = arrivalType,
                    arrivalCustomName = arrivalCustomName,
                    departureDate = departureDate,
                    departureTime = departureTime,
                    maxParticipants = maxParticipants
                )
            ).toCallvanPostCreate()
        }.mapHttpFailure {
            on(400, "INVALID_REQUEST_BODY") throws KoinCallvanException.InvalidRequestBodyException()
            on(400, "INVALID_CUSTOM_LOCATION_NAME") throws KoinCallvanException.InvalidCustomLocationNameException()
            on(404) throws KoinCallvanException.NotFoundUserException()
        }
    }

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
    ): Result<CallvanPostSearch> {
        return runCatching {
            callvanRemoteDataSource.getCallvanPosts(
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
            ).toCallvanPostSearch()
        }.mapHttpFailure {
            on(401) throws KoinCallvanException.UnauthorizedUserException()
            on(404) throws KoinCallvanException.NotFoundUserException()
        }
    }

    override suspend fun sendMessage(
        postId: Int,
        isImage: Boolean,
        content: String
    ): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.sendMessage(
                postId = postId,
                callvanChatMessageRequest = CallvanChatMessageRequest(
                    isImage = isImage,
                    content = content
                )
            )
        }.mapHttpFailure {
            on(403) throws KoinCallvanException.ForbiddenParticipantException()
            on(404) throws KoinCallvanException.NotFoundArticleException()
        }
    }

    override suspend fun getCallvanChatMessages(
        postId: Int
    ): Result<CallvanChatMessage> {
        return runCatching {
            callvanRemoteDataSource.getCallvanChatMessages(
                postId = postId
            ).toCallvanChatMessage()
        }.mapHttpFailure {
            on(403) throws KoinCallvanException.ForbiddenParticipantException()
            on(404) throws KoinCallvanException.NotFoundArticleException()
        }
    }

    override suspend fun getCallvanPostDetail(
        postId: Int
    ): Result<CallvanPostDetail> {
        return runCatching {
            callvanRemoteDataSource.getCallvanPostDetail(
                postId = postId
            ).toCallvanPostDetail()
        }.mapHttpFailure {
            on(403) throws KoinCallvanException.ForbiddenParticipantException()
            on(404) throws KoinCallvanException.NotFoundArticleException()
        }
    }

    override suspend fun reportCallvanUser(
        postId: Int,
        reportedUserId: Int,
        description: String?,
        reasons: List<Pair<String, String?>>,
        attachmentUrls: List<String>?
    ): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.reportCallvanUser(
                postId = postId,
                callvanUserReportCreateRequest = CallvanUserReportCreateRequest(
                    reportedUserId = reportedUserId,
                    description = description,
                    reasons = reasons.map {
                        CallvanUserReportCreateRequest.CallvanUserReportReasonRequest(
                            reasonCode = it.first,
                            customText = it.second
                        )
                    },
                    attachments = attachmentUrls?.map {
                        CallvanUserReportCreateRequest.CallvanUserReportAttachmentRequest(
                            attachmentType = "IMAGE",
                            url = it
                        )
                    }
                )
            )
        }.mapHttpFailure {
            on(400, "INVALID_REQUEST_BODY") throws KoinCallvanException.InvalidRequestBodyException()
            on(400, "CALLVAN_REPORT_SELF") throws KoinCallvanException.CallvanReportSelfException()
            on(403) throws KoinCallvanException.CallvanReportOnlyParticipantException()
            on(404, "NOT_FOUND_ARTICLE") throws KoinCallvanException.NotFoundArticleException()
            on(404, "NOT_FOUND_USER") throws KoinCallvanException.NotFoundUserException()
            on(409) throws KoinCallvanException.CallvanReportAlreadyPendingException()
        }
    }

    override suspend fun closeCallvanPost(
        postId: Int
    ): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.closeCallvanPost(
                postId = postId
            )
        }.mapHttpFailure {
            on(403) throws KoinCallvanException.ForbiddenAuthorException()
            on(404) throws KoinCallvanException.NotFoundArticleException()
        }
    }

    override suspend fun getNotifications(): Result<List<CallvanNotification>> {
        return runCatching {
            callvanRemoteDataSource.getNotifications().map { it.toCallvanNotification() }
        }
    }

    override suspend fun deleteAllNotifications(): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.deleteAllNotifications()
        }
    }

    override suspend fun deleteNotification(
        notificationId: Int
    ): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.deleteNotification(
                notificationId = notificationId
            )
        }
    }

    override suspend fun markNotificationAsRead(
        notificationId: Int
    ): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.markNotificationAsRead(
                notificationId = notificationId
            )
        }
    }

    override suspend fun markAllNotificationsAsRead(): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.markAllNotificationsAsRead()
        }
    }

    override suspend fun completeCallvanPost(
        postId: Int
    ): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.completeCallvanPost(
                postId = postId
            )
        }.mapHttpFailure {
            on(403) throws KoinCallvanException.ForbiddenAuthorException()
            on(404) throws KoinCallvanException.NotFoundArticleException()
        }
    }

    override suspend fun joinCallvanPost(
        postId: Int
    ): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.joinCallvanPost(
                postId = postId
            )
        }.mapHttpFailure {
            on(400, "CALLVAN_POST_NOT_RECRUITING") throws KoinCallvanException.CallvanPostNotRecruitingException()
            on(400, "CALLVAN_POST_FULL") throws KoinCallvanException.CallvanPostFullException()
            on(404) throws KoinCallvanException.NotFoundArticleException()
            on(409) throws KoinCallvanException.CallvanAlreadyJoinedException()
        }
    }

    override suspend fun leaveCallvanPost(
        postId: Int
    ): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.leaveCallvanPost(
                postId = postId
            )
        }.mapHttpFailure {
            on(400) throws KoinCallvanException.CallvanPostAuthorException()
            on(403) throws KoinCallvanException.ForbiddenParticipantException()
            on(404) throws KoinCallvanException.NotFoundArticleException()
        }
    }

    override suspend fun reopenCallvanPost(
        postId: Int
    ): Result<Unit> {
        return runCatching {
            callvanRemoteDataSource.reopenCallvanPost(
                postId = postId
            )
        }.mapHttpFailure {
            on(400, "CALLVAN_POST_REOPEN_FAILED_FULL") throws KoinCallvanException.CallvanPostReopenFailedFullException()
            on(400, "CALLVAN_POST_REOPEN_FAILED_TIME") throws KoinCallvanException.CallvanPostReopenFailedTimeException()
            on(403) throws KoinCallvanException.ForbiddenAuthorException()
            on(404) throws KoinCallvanException.NotFoundArticleException()
        }
    }

    override suspend fun getCallvanRestriction(): Result<CallvanRestriction> {
        return runCatching {
            callvanRemoteDataSource.getCallvanRestriction().toCallvanRestriction()
        }.mapHttpFailure {
            on(401) throws KoinCallvanException.UnauthorizedUserException()
            on(404) throws KoinCallvanException.NotFoundUserException()
        }
    }
}
