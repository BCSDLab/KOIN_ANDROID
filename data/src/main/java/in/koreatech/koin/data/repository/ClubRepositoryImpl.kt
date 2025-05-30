package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toClubCategories
import `in`.koreatech.koin.data.mapper.toClubDetails
import `in`.koreatech.koin.data.mapper.toClubHot
import `in`.koreatech.koin.data.mapper.toClubQnasInfo
import `in`.koreatech.koin.data.mapper.toClubs
import `in`.koreatech.koin.data.request.club.ClubEmpowermentRequest
import `in`.koreatech.koin.data.request.club.ClubQnaRequest
import `in`.koreatech.koin.data.source.remote.ClubRemoteDataSource
import `in`.koreatech.koin.data.util.getErrorResponse
import `in`.koreatech.koin.data.util.toKoinUnknownErrorException
import `in`.koreatech.koin.domain.error.club.ClubError
import `in`.koreatech.koin.domain.model.club.ClubCategories
import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubHot
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.domain.model.club.Clubs
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject
import retrofit2.HttpException

class ClubRepositoryImpl @Inject constructor(
    private val clubRemoteDataSource: ClubRemoteDataSource
) : ClubRepository {
    override suspend fun getClubsCategories(): Result<ClubCategories> {
        return runCatching {
            clubRemoteDataSource.getClubsCategories().toClubCategories()
        }
    }

    override suspend fun getClubHot(): Result<ClubHot> {
        return runCatching {
            clubRemoteDataSource.getClubHot().toClubHot()
        }
    }

    override suspend fun getClubs(categoryId: Int?, sortType: String?): Result<Clubs> {
        return runCatching {
            clubRemoteDataSource.getClubs(categoryId, sortType).toClubs()
        }
    }

    override suspend fun cancelClubLike(clubId: Int): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.cancelClubLike(clubId)
        }
    }

    override suspend fun getClubDetails(clubId: Int): Result<ClubDetails> {
        return runCatching {
            clubRemoteDataSource.getClubDetails(clubId).toClubDetails()
        }
    }

    override suspend fun getClubQnas(clubId: Int): Result<ClubQnasInfo> {
        return runCatching {
            clubRemoteDataSource.getClubQnas(clubId).toClubQnasInfo()
        }
    }

    override suspend fun setClubLike(clubId: Int): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.setClubLike(clubId)
        }
    }

    override suspend fun deleteClubQna(clubId: Int, qnaId: Int): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.deleteClubQna(clubId, qnaId)
        }
    }

    override suspend fun setClubEmpowerment(clubId: Int, changedManagerId: String): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.setClubEmpowerment(ClubEmpowermentRequest(clubId, changedManagerId))
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
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
        return runCatching {
            clubRemoteDataSource.postClubQna(
                clubId,
                ClubQnaRequest(parentId, content)
            )
        }
    }
}
