package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toMyAppliedRecruitments
import `in`.koreatech.koin.data.mapper.toMyRecruitmentPosts
import `in`.koreatech.koin.data.mapper.toRecruitmentDetail
import `in`.koreatech.koin.data.mapper.toRecruitmentNotifications
import `in`.koreatech.koin.data.mapper.toRecruitmentUpdateRequest
import `in`.koreatech.koin.data.mapper.toRecruitments
import `in`.koreatech.koin.data.mapper.toTeamRecruitmentActivityRequest
import `in`.koreatech.koin.data.mapper.toTeamRecruitmentApplication
import `in`.koreatech.koin.data.mapper.toTeamRecruitmentProfile
import `in`.koreatech.koin.data.mapper.toTeamRecruitmentRoleRequest
import `in`.koreatech.koin.data.request.recruitment.TeamRecruitmentApplicationRequest
import `in`.koreatech.koin.data.request.recruitment.TeamRecruitmentCreateRequest
import `in`.koreatech.koin.data.request.recruitment.TeamRecruitmentProfileRequest
import `in`.koreatech.koin.data.source.remote.RecruitmentRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentException
import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitments
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPosts
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentDetail
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentUpdate
import `in`.koreatech.koin.domain.model.recruitment.Recruitments
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivityInput
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentApplication
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentRoleInput
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

    // ↓↓↓ create/apply/profile/profilecreate 화면과 무관한 기존 메서드입니다. 손대지 않았습니다. ↓↓↓
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
    ): Result<MyRecruitmentPosts> {
        return suspendRunCatching {
            recruitmentRemoteDataSource
                .getMyRecruitmentPosts(status, sort, page, limit)
                .toMyRecruitmentPosts()
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
    ): Result<MyAppliedRecruitments> {
        return suspendRunCatching {
            recruitmentRemoteDataSource
                .getMyAppliedRecruitments(statuses, sort, page, limit)
                .toMyAppliedRecruitments()
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

    override suspend fun getTeamRecruitmentProfile(): Result<TeamRecruitmentProfile> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.getTeamRecruitmentProfile().toTeamRecruitmentProfile()
        }.mapHttpFailure {
            on(401) throws KoinRecruitmentException.UnauthorizedUserException(errorResponse.message)
            on(403, "FORBIDDEN_USER_TYPE") throws KoinRecruitmentException.ForbiddenUserTypeException(errorResponse.message)
            on(404, "NOT_FOUND_USER") throws KoinRecruitmentException.NotFoundUserException(errorResponse.message)
            on(404, "TEAM_RECRUITMENT_PROFILE_NOT_FOUND") throws
                KoinRecruitmentException.ProfileNotFoundException(errorResponse.message)
        }
    }

    override suspend fun saveTeamRecruitmentProfile(
        profileNickname: String,
        preferredRole: String,
        skills: List<String>,
        activities: List<TeamRecruitmentActivityInput>,
        selfIntroduction: String
    ): Result<TeamRecruitmentProfile> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.saveTeamRecruitmentProfile(
                TeamRecruitmentProfileRequest(
                    profileNickname = profileNickname,
                    preferredRole = preferredRole,
                    skills = skills,
                    activities = activities.map { it.toTeamRecruitmentActivityRequest() },
                    selfIntroduction = selfIntroduction
                )
            ).toTeamRecruitmentProfile()
        }.mapHttpFailure {
            on(400, "TEAM_RECRUITMENT_ACTIVITY_END_DATE_REQUIRED") throws
                KoinRecruitmentException.ActivityEndDateRequiredException(errorResponse.message)
            on(400, "TEAM_RECRUITMENT_ACTIVITY_END_DATE_MUST_BE_NULL") throws
                KoinRecruitmentException.ActivityEndDateMustBeNullException(errorResponse.message)
            on(400, "INVALID_START_DATE_AFTER_END_DATE") throws
                KoinRecruitmentException.InvalidStartDateAfterEndDateException(errorResponse.message)
            on(400, "INVALID_REQUEST_BODY") throws KoinRecruitmentException.InvalidRequestBodyException(errorResponse.message)
            on(401) throws KoinRecruitmentException.UnauthorizedUserException(errorResponse.message)
            on(403, "FORBIDDEN_USER_TYPE") throws KoinRecruitmentException.ForbiddenUserTypeException(errorResponse.message)
            on(404, "NOT_FOUND_USER") throws KoinRecruitmentException.NotFoundUserException(errorResponse.message)
        }
    }

    override suspend fun createTeamRecruitment(
        category: String,
        title: String,
        meetingType: String,
        activityStartDate: String,
        activityEndDate: String,
        deadlineDate: String,
        recruitmentType: String,
        maxParticipants: Int?,
        roles: List<TeamRecruitmentRoleInput>,
        description: String,
        relatedUrl: String?,
        qualification: String?
    ): Result<Int> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.createTeamRecruitment(
                TeamRecruitmentCreateRequest(
                    category = category,
                    title = title,
                    meetingType = meetingType,
                    activityStartDate = activityStartDate,
                    activityEndDate = activityEndDate,
                    deadlineDate = deadlineDate,
                    recruitmentType = recruitmentType,
                    maxParticipants = maxParticipants,
                    roles = roles.map { it.toTeamRecruitmentRoleRequest() },
                    description = description,
                    relatedUrl = relatedUrl,
                    qualification = qualification
                )
            ).id
        }.mapHttpFailure {
            on(400, "TEAM_RECRUITMENT_INVALID_DEADLINE_DATE") throws
                KoinRecruitmentException.InvalidDeadlineDateException(errorResponse.message)
            on(400, "TEAM_RECRUITMENT_INVALID_ROLE_COMPOSITION") throws
                KoinRecruitmentException.InvalidRoleCompositionException(errorResponse.message)
            on(400, "INVALID_START_DATE_AFTER_END_DATE") throws
                KoinRecruitmentException.InvalidStartDateAfterEndDateException(errorResponse.message)
            on(400, "INVALID_REQUEST_BODY") throws KoinRecruitmentException.InvalidRequestBodyException(errorResponse.message)
            on(401) throws KoinRecruitmentException.UnauthorizedUserException(errorResponse.message)
            on(403, "FORBIDDEN_USER_TYPE") throws KoinRecruitmentException.ForbiddenUserTypeException(errorResponse.message)
            on(404, "NOT_FOUND_USER") throws KoinRecruitmentException.NotFoundUserException(errorResponse.message)
        }
    }

    override suspend fun applyTeamRecruitment(
        recruitmentId: Int,
        roleId: Int,
        motivation: String,
        availability: String
    ): Result<TeamRecruitmentApplication> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.applyTeamRecruitment(
                recruitmentId = recruitmentId,
                request = TeamRecruitmentApplicationRequest(
                    roleId = roleId,
                    motivation = motivation,
                    availability = availability
                )
            ).toTeamRecruitmentApplication()
        }.mapHttpFailure {
            on(400, "ILLEGAL_ARGUMENT") throws KoinRecruitmentException.InvalidArgumentException(errorResponse.message)
            on(400, "INVALID_REQUEST_BODY") throws KoinRecruitmentException.InvalidRequestBodyException(errorResponse.message)
            on(400, "NOT_READABLE_HTTP_MESSAGE") throws
                KoinRecruitmentException.NotReadableHttpMessageException(errorResponse.message)
            on(401) throws KoinRecruitmentException.UnauthorizedUserException(errorResponse.message)
            on(403, "TEAM_RECRUITMENT_FORBIDDEN") throws
                KoinRecruitmentException.RecruitmentForbiddenException(errorResponse.message)
            on(404, "TEAM_RECRUITMENT_NOT_FOUND") throws
                KoinRecruitmentException.RecruitmentNotFoundException(errorResponse.message)
            on(409, "TEAM_RECRUITMENT_PROFILE_REQUIRED") throws
                KoinRecruitmentException.ProfileRequiredException(errorResponse.message)
            on(409, "TEAM_RECRUITMENT_CLOSED") throws KoinRecruitmentException.RecruitmentClosedException(errorResponse.message)
            on(409, "TEAM_RECRUITMENT_ROLE_CLOSED") throws
                KoinRecruitmentException.RecruitmentRoleClosedException(errorResponse.message)
            on(409, "TEAM_RECRUITMENT_CAPACITY_FULL") throws KoinRecruitmentException.CapacityFullException(errorResponse.message)
            on(409, "TEAM_RECRUITMENT_APPLICATION_DUPLICATE") throws
                KoinRecruitmentException.ApplicationDuplicateException(errorResponse.message)
            on(409, "REQUEST_TOO_FAST") throws KoinRecruitmentException.RequestTooFastException(errorResponse.message)
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
