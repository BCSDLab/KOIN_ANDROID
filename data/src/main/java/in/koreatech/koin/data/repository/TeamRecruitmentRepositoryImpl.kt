package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toMyRecruitmentPost
import `in`.koreatech.koin.data.source.remote.TeamRecruitmentRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.data.util.suspendRunCatching
import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentException
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.repository.TeamRecruitmentRepository
import javax.inject.Inject

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
        }.mapHttpFailure {
            on(400, "ILLEGAL_ARGUMENT") throws KoinRecruitmentException.IllegalArgumentException()
            on(401) throws KoinRecruitmentException.UnauthorizedUserException()
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun closeRecruitmentPost(postId: Int): Result<Unit> {
        return suspendRunCatching {
            teamRecruitmentRemoteDataSource.closeRecruitmentPost(postId)
        }.mapHttpFailure {
            on(401) throws KoinRecruitmentException.UnauthorizedUserException()
            on(403) throws KoinRecruitmentException.ForbiddenException()
            on(404) throws KoinRecruitmentException.NotFoundException()
        }
    }
}
