package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentDetail
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications
import `in`.koreatech.koin.domain.model.recruitment.Recruitments

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

    suspend fun closeRecruitmentPost(postId: Int): Result<Unit>
}
