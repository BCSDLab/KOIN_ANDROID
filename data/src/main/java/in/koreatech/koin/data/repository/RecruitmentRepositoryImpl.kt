package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toMyAppliedRecruitment
import `in`.koreatech.koin.data.mapper.toMyRecruitmentPost
import `in`.koreatech.koin.data.mapper.toRecruitmentDetail
import `in`.koreatech.koin.data.mapper.toRecruitmentNotifications
import `in`.koreatech.koin.data.mapper.toRecruitmentUpdateRequest
import `in`.koreatech.koin.data.mapper.toRecruitments
import `in`.koreatech.koin.data.source.remote.RecruitmentRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentException
import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentDetail
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentUpdate
import `in`.koreatech.koin.domain.model.recruitment.Recruitments
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class RecruitmentRepositoryImpl @Inject constructor(
    private val recruitmentRemoteDataSource: RecruitmentRemoteDataSource
) : RecruitmentRepository {
    @Suppress("LongParameterList")
    override suspend fun getRecruitments(
        keyword: String?,
        status: String?,
        categories: List<String>?,
        meetingType: String?,
        sort: String?,
        page: Int,
        limit: Int
    ): Result<Recruitments> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.getRecruitments(
                keyword = keyword,
                status = status,
                categories = categories,
                meetingType = meetingType,
                sort = sort,
                page = page,
                limit = limit
            ).toRecruitments()
        }.mapHttpFailure {
            on(400) throws KoinRecruitmentException.InvalidRequestException()
        }
    }

    override suspend fun getRecruitmentDetail(recruitmentId: Int): Result<RecruitmentDetail> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.getRecruitmentDetail(
                recruitmentId = recruitmentId
            ).toRecruitmentDetail()
        }.mapHttpFailure {
            on(404) throws KoinRecruitmentException.NotFoundException()
        }
    }

    override suspend fun deleteRecruitment(recruitmentId: Int): Result<Unit> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.deleteRecruitment(recruitmentId = recruitmentId)
        }.mapHttpFailure {
            on(401) throws KoinRecruitmentException.UnauthorizedException()
            on(403) throws KoinRecruitmentException.ForbiddenException()
            on(404) throws KoinRecruitmentException.NotFoundException()
        }
    }

    override suspend fun getNotifications(page: Int, limit: Int): Result<RecruitmentNotifications> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.getNotifications(
                page = page,
                limit = limit
            ).toRecruitmentNotifications()
        }.mapHttpFailure {
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun deleteAllNotifications(): Result<Unit> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.deleteAllNotifications()
        }.mapHttpFailure {
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun readNotification(notificationId: Int): Result<Unit> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.readNotification(
                notificationId = notificationId
            )
        }.mapHttpFailure {
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun readAllNotifications(): Result<Unit> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.readAllNotifications()
        }.mapHttpFailure {
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun getMyRecruitmentPosts(
        status: String,
        sort: String,
        page: Int,
        limit: Int
    ): Result<List<MyRecruitmentPost>> {
        return suspendRunCatching {
            recruitmentRemoteDataSource
                .getMyRecruitmentPosts(status, sort, page, limit)
                .recruitments
                .map { it.toMyRecruitmentPost() }
        }.mapHttpFailure {
            on(400, "ILLEGAL_ARGUMENT") throws KoinRecruitmentException.InvalidArgumentException()
            on(401) throws KoinRecruitmentException.UnauthorizedUserException()
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun getMyAppliedRecruitments(
        statuses: List<String>,
        sort: String,
        page: Int,
        limit: Int
    ): Result<List<MyAppliedRecruitment>> {
        return suspendRunCatching {
            recruitmentRemoteDataSource
                .getMyAppliedRecruitments(statuses, sort, page, limit)
                .applications
                .map { it.toMyAppliedRecruitment() }
        }.mapHttpFailure {
            on(400, "ILLEGAL_ARGUMENT") throws KoinRecruitmentException.InvalidArgumentException()
            on(401) throws KoinRecruitmentException.UnauthorizedUserException()
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun closeRecruitmentPost(postId: Int): Result<Unit> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.closeRecruitmentPost(postId)
        }.mapHttpFailure {
            on(401) throws KoinRecruitmentException.UnauthorizedUserException()
            on(403) throws KoinRecruitmentException.ForbiddenException()
            on(404) throws KoinRecruitmentException.NotFoundException()
        }
    }

    override suspend fun updateRecruitment(recruitmentId: Int, update: RecruitmentUpdate): Result<RecruitmentDetail> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.updateRecruitment(
                recruitmentId = recruitmentId,
                request = update.toRecruitmentUpdateRequest()
            ).toRecruitmentDetail()
        }.mapHttpFailure {
            on(400, "TEAM_RECRUITMENT_INVALID_DEADLINE_DATE") throws KoinRecruitmentException.InvalidDeadlineDateException()
            on(400, "TEAM_RECRUITMENT_INVALID_ROLE_COMPOSITION") throws KoinRecruitmentException.InvalidRoleCompositionException()
            on(400, "INVALID_START_DATE_AFTER_END_DATE") throws KoinRecruitmentException.InvalidStartDateAfterEndDateException()
            on(400, "INVALID_REQUEST_BODY") throws KoinRecruitmentException.InvalidRequestBodyException()
            on(401, "UNAUTHORIZED_USER") throws KoinRecruitmentException.UnauthorizedUserException()
            on(403, "TEAM_RECRUITMENT_FORBIDDEN") throws KoinRecruitmentException.ForbiddenException()
            on(403, "FORBIDDEN_USER_TYPE") throws KoinRecruitmentException.ForbiddenUserTypeException()
            on(404, "TEAM_RECRUITMENT_NOT_FOUND") throws KoinRecruitmentException.NotFoundException()
            on(404, "TEAM_RECRUITMENT_ROLE_NOT_FOUND") throws KoinRecruitmentException.RoleNotFoundException()
            on(409, "TEAM_RECRUITMENT_CLOSED") throws KoinRecruitmentException.RecruitmentClosedException()
            on(409, "TEAM_RECRUITMENT_ROLE_UPDATE_NOT_ALLOWED") throws KoinRecruitmentException.RoleUpdateNotAllowedException()
            on(409, "TEAM_RECRUITMENT_MAX_PARTICIPANTS_BELOW_ACCEPTED") throws
                KoinRecruitmentException.MaxParticipantsBelowAcceptedException()
            on(409, "TEAM_RECRUITMENT_TYPE_CHANGE_NOT_ALLOWED") throws KoinRecruitmentException.RecruitmentTypeChangeNotAllowedException()
        }
    }
}
