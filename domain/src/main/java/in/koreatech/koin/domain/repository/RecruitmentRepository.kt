package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost

interface RecruitmentRepository {

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
}
