package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentDetail
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentUpdate
import `in`.koreatech.koin.domain.model.recruitment.Recruitments
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivityInput
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentApplication
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentRoleInput

interface RecruitmentRepository {
    @Suppress("LongParameterList")
    suspend fun getRecruitments(
        keyword: String?,
        status: String?,
        categories: List<String>?,
        meetingType: String?,
        sort: String?,
        page: Int,
        limit: Int
    ): Result<Recruitments>

    suspend fun getRecruitmentDetail(recruitmentId: Int): Result<RecruitmentDetail>

    suspend fun deleteRecruitment(recruitmentId: Int): Result<Unit>

    suspend fun updateRecruitment(recruitmentId: Int, update: RecruitmentUpdate): Result<RecruitmentDetail>

    suspend fun getNotifications(page: Int, limit: Int): Result<RecruitmentNotifications>

    suspend fun deleteAllNotifications(): Result<Unit>

    suspend fun readNotification(notificationId: Int): Result<Unit>

    suspend fun readAllNotifications(): Result<Unit>

    suspend fun getMyRecruitmentPosts(
        status: String,
        sort: String,
        page: Int,
        limit: Int
    ): Result<List<MyRecruitmentPost>>

    suspend fun getMyAppliedRecruitments(
        statuses: List<String>,
        sort: String,
        page: Int,
        limit: Int
    ): Result<List<MyAppliedRecruitment>>

    suspend fun closeRecruitmentPost(postId: Int): Result<Unit>

    suspend fun getTeamRecruitmentProfile(): Result<TeamRecruitmentProfile>

    suspend fun saveTeamRecruitmentProfile(
        profileNickname: String,
        preferredRole: String,
        skills: List<String>,
        activities: List<TeamRecruitmentActivityInput>,
        selfIntroduction: String
    ): Result<TeamRecruitmentProfile>

    suspend fun createTeamRecruitment(
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
    ): Result<Int>

    suspend fun applyTeamRecruitment(
        recruitmentId: Int,
        roleId: Int,
        motivation: String,
        availability: String
    ): Result<TeamRecruitmentApplication>
}
