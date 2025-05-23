package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ClubApi
import `in`.koreatech.koin.data.api.auth.ClubAuthApi
import `in`.koreatech.koin.data.mapper.httpExceptionMapper
import `in`.koreatech.koin.data.request.club.ClubEmpowermentRequest
import `in`.koreatech.koin.data.request.club.ClubQnaRequest
import `in`.koreatech.koin.data.response.club.ClubDetailsResponse
import `in`.koreatech.koin.data.response.club.ClubQnasResponse
import javax.inject.Inject
import retrofit2.HttpException

class ClubRemoteDataSource @Inject constructor(
    private val clubApi: ClubApi,
    private val clubAuthApi: ClubAuthApi
) {
    suspend fun getClubDetails(clubId: Int): Result<ClubDetailsResponse> {
        return runCatching {
            clubAuthApi.getClubDetails(clubId)
        }.onFailure { e ->
            if (e is HttpException) {
                e.httpExceptionMapper()
            } else {
                throw e
            }
        }
    }

    suspend fun getClubQnas(clubId: Int): Result<ClubQnasResponse> {
        return runCatching {
            clubApi.getClubQnas(clubId)
        }.onFailure { e ->
            if (e is HttpException) {
                e.httpExceptionMapper()
            } else {
                throw e
            }
        }
    }

    suspend fun setClubEmpowerment(request: ClubEmpowermentRequest): Result<Unit> {
        return runCatching {
            val response = clubAuthApi.setClubEmpowerment(request)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { e ->
            if (e is HttpException) {
                e.httpExceptionMapper()
            } else {
                throw e
            }
        }
    }

    suspend fun setClubLike(clubId: Int): Result<Unit> {
        return runCatching {
            val response = clubAuthApi.setClubLike(clubId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { e ->
            if (e is HttpException) {
                e.httpExceptionMapper()
            } else {
                throw e
            }
        }
    }

    suspend fun postClubQna(
        clubId: Int,
        request: ClubQnaRequest
    ): Result<Unit> {
        return runCatching {
            val response = clubAuthApi.postClubQna(clubId, request)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { e ->
            if (e is HttpException) {
                e.httpExceptionMapper()
            } else {
                throw e
            }
        }
    }

    suspend fun deleteClubQna(
        clubId: Int,
        qnaId: Int
    ): Result<Unit> {
        return runCatching {
            val response = clubAuthApi.deleteClubQna(clubId, qnaId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { e ->
            if (e is HttpException) {
                e.httpExceptionMapper()
            } else {
                throw e
            }
        }
    }

    suspend fun cancelClubLike(clubId: Int): Result<Unit> {
        return runCatching {
            val response = clubAuthApi.cancelClubLike(clubId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { e ->
            if (e is HttpException) {
                e.httpExceptionMapper()
            } else {
                throw e
            }
        }
    }
}
