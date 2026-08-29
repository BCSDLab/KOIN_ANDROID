package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.recruitment.RecruitmentPostInfo

interface TeamRecruitmentRepository {

    suspend fun getMyRecruitmentPosts(
        status: String,
        sort: String,
        page: Int,
        limit: Int
    ): Result<List<RecruitmentPostInfo>>

    suspend fun closeRecruitmentPost(postId: Int): Result<Unit>
}
