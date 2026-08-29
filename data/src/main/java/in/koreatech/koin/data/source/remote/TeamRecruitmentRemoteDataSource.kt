package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.RecruitmentAuthApi
import `in`.koreatech.koin.data.response.recruitment.MyAppliedRecruitmentListResponse
import `in`.koreatech.koin.data.response.recruitment.MyRecruitmentListResponse
import javax.inject.Inject
import retrofit2.Response

class TeamRecruitmentRemoteDataSource @Inject constructor(
    private val recruitmentAuthApi: RecruitmentAuthApi
) {
    suspend fun getMyRecruitmentPosts(
        status: String,
        sort: String,
        page: Int,
        limit: Int
    ): MyRecruitmentListResponse = recruitmentAuthApi.getMyRecruitmentPosts(status, sort, page, limit)

    suspend fun getMyAppliedRecruitments(
        statuses: List<String>,
        sort: String,
        page: Int,
        limit: Int
    ): MyAppliedRecruitmentListResponse = recruitmentAuthApi.getMyAppliedRecruitments(statuses, sort, page, limit)

    suspend fun closeRecruitmentPost(postId: Int): Response<Unit> =
        recruitmentAuthApi.closeRecruitmentPost(postId)
}
