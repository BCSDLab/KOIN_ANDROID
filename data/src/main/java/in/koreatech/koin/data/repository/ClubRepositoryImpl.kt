package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toClubDetails
import `in`.koreatech.koin.data.mapper.toClubQnasInfo
import `in`.koreatech.koin.data.request.club.ClubEmpowermentRequest
import `in`.koreatech.koin.data.request.club.ClubQnaRequest
import `in`.koreatech.koin.data.source.remote.ClubRemoteDataSource
import `in`.koreatech.koin.data.util.getErrorResponse
import `in`.koreatech.koin.data.util.toKoinUnknownErrorException
import `in`.koreatech.koin.domain.error.KoinErrorException
import `in`.koreatech.koin.domain.error.KoinUnknownErrorException
import `in`.koreatech.koin.domain.error.club.ClubError
import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.domain.repository.ClubRepository
import retrofit2.HttpException
import javax.inject.Inject

class ClubRepositoryImpl @Inject constructor(
    private val clubRemoteDataSource: ClubRemoteDataSource
) : ClubRepository {
    override suspend fun cancelClubLike(clubId: Int): Result<Unit> {
        return clubRemoteDataSource.cancelClubLike(clubId)
    }

    override suspend fun getClubDetails(clubId: Int): Result<ClubDetails> {
        return clubRemoteDataSource.getClubDetails(clubId).map { it.toClubDetails() }
    }

    override suspend fun getClubQnas(clubId: Int): Result<ClubQnasInfo> {
        return clubRemoteDataSource.getClubQnas(clubId).map { it.toClubQnasInfo() }
    }

    override suspend fun setClubLike(clubId: Int): Result<Unit> {
        return clubRemoteDataSource.setClubLike(clubId)
    }

    override suspend fun deleteClubQna(clubId: Int, qnaId: Int): Result<Unit> {
        return clubRemoteDataSource.deleteClubQna(clubId, qnaId)
    }

    override suspend fun setClubEmpowerment(clubId: Int, changedManagerId: String): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.setClubEmpowerment(ClubEmpowermentRequest(clubId, changedManagerId))
            if (response.isSuccessful) Unit
            else throw HttpException(response)
        }.recoverCatching { e ->
            if (e is HttpException) {
                when (e.code()) {
                    401 -> throw ClubError.Unauthorized
                    403 -> throw ClubError.Forbidden
                    404 -> throw ClubError.NotFoundUserId
                    else -> throw e.getErrorResponse().toKoinUnknownErrorException()
                }
            }
        }
    }

    override suspend fun postClubQna(clubId: Int, parentId: Int?, content: String): Result<Unit> {
        return clubRemoteDataSource.postClubQna(
            clubId,
            ClubQnaRequest(parentId, content)
        )
    }
}
