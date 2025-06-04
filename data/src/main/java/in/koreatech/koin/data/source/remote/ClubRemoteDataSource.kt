package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ClubApi
import `in`.koreatech.koin.data.api.auth.ClubAuthApi
import `in`.koreatech.koin.data.request.club.ClubCreateRequest
import `in`.koreatech.koin.data.request.club.ClubEmpowermentRequest
import `in`.koreatech.koin.data.request.club.ClubModifyRequest
import `in`.koreatech.koin.data.request.club.ClubQnaRequest
import javax.inject.Inject
import retrofit2.HttpException

class ClubRemoteDataSource @Inject constructor(
    private val clubApi: ClubApi,
    private val clubAuthApi: ClubAuthApi
) {
    suspend fun getClubsCategories() = clubApi.getClubsCategories()

    suspend fun getClubHot() = clubApi.getClubHot()

    suspend fun getClubs(
        categoryId: Int? = null,
        sortType: String? = null
    ) = clubAuthApi.getClubs(categoryId, sortType)

    suspend fun getClubDetails(clubId: Int) = clubAuthApi.getClubDetails(clubId)

    suspend fun createClub(request: ClubCreateRequest) {
        clubAuthApi.createClub(request).let {
            if (!it.isSuccessful) {
                throw HttpException(it)
            }
        }
    }

    suspend fun modifyClub(
        clubId: Int,
        request: ClubModifyRequest
    ) {
        clubAuthApi.modifyClub(clubId, request).let {
            if (!it.isSuccessful) {
                throw HttpException(it)
            }
        }
    }

    suspend fun getClubQnas(clubId: Int) = clubApi.getClubQnas(clubId)

    suspend fun setClubEmpowerment(request: ClubEmpowermentRequest) = clubAuthApi.setClubEmpowerment(request)

    suspend fun setClubLike(clubId: Int) = clubAuthApi.setClubLike(clubId)

    suspend fun postClubQna(
        clubId: Int,
        request: ClubQnaRequest
    ) = clubAuthApi.postClubQna(clubId, request)

    suspend fun deleteClubQna(
        clubId: Int,
        qnaId: Int
    ) = clubAuthApi.deleteClubQna(clubId, qnaId)

    suspend fun cancelClubLike(clubId: Int) = clubAuthApi.cancelClubLike(clubId)
}
