package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications

interface RecruitmentRepository {
    suspend fun getNotifications(page: Int, limit: Int): Result<RecruitmentNotifications>

    suspend fun getMyRecruitmentPosts(
        status: String,
        sort: String,
        page: Int,
        limit: Int
    ): Result<List<MyRecruitmentPost>>
    suspend fun deleteAllNotifications(): Result<Unit>

    suspend fun getMyAppliedRecruitments(
        statuses: List<String>,
        sort: String,
        page: Int,
        limit: Int
    ): Result<List<MyAppliedRecruitment>>

    suspend fun readNotification(notificationId: Int): Result<Unit>

    suspend fun closeRecruitmentPost(postId: Int): Result<Unit>

    suspend fun readAllNotifications(): Result<Unit>
}
