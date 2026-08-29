package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toMyRecruitmentPost
import `in`.koreatech.koin.data.source.remote.TeamRecruitmentRemoteDataSource
import `in`.koreatech.koin.data.util.suspendRunCatching
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.repository.TeamRecruitmentRepository
import javax.inject.Inject
import retrofit2.HttpException

class TeamRecruitmentRepositoryImpl @Inject constructor(
    private val teamRecruitmentRemoteDataSource: TeamRecruitmentRemoteDataSource
) : TeamRecruitmentRepository {

    override suspend fun getMyRecruitmentPosts(
        status: String,
        sort: String,
        page: Int,
        limit: Int
    ): Result<List<MyRecruitmentPost>> {
        return suspendRunCatching {
            teamRecruitmentRemoteDataSource
                .getMyRecruitmentPosts(status, sort, page, limit)
                .recruitments
                .map { it.toMyRecruitmentPost() }
        }
    }

    override suspend fun closeRecruitmentPost(postId: Int): Result<Unit> {
        return suspendRunCatching {
            val response = teamRecruitmentRemoteDataSource.closeRecruitmentPost(postId)
            if (!response.isSuccessful) throw HttpException(response)
        }
    }
}
