package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ClubApi
import `in`.koreatech.koin.data.api.auth.ClubAuthApi
import `in`.koreatech.koin.data.request.club.ClubCreateRequest
import `in`.koreatech.koin.data.request.club.ClubEmpowermentRequest
import `in`.koreatech.koin.data.request.club.ClubEventRequest
import `in`.koreatech.koin.data.request.club.ClubModifyRequest
import `in`.koreatech.koin.data.request.club.ClubQnaRequest
import `in`.koreatech.koin.data.request.club.ClubRecruitmentRequest
import javax.inject.Inject

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
        clubAuthApi.createClub(request)
    }

    suspend fun modifyClub(
        clubId: Int,
        request: ClubModifyRequest
    ) {
        clubAuthApi.modifyClub(clubId, request)
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

    suspend fun getClubRecruitment(clubId: Int) = clubApi.getClubRecruitment(clubId)

    suspend fun createClubRecruitment(
        clubId: Int,
        request: ClubRecruitmentRequest
    ) = clubAuthApi.createClubRecruitment(clubId, request)

    suspend fun deleteClubRecruitment(clubId: Int) = clubAuthApi.deleteClubRecruitment(clubId)

    suspend fun modifyClubRecruitment(
        clubId: Int,
        request: ClubRecruitmentRequest
    ) = clubAuthApi.modifyClubRecruitment(clubId, request)

    suspend fun getClubEvents(
        clubId: Int,
        eventType: String
    ) = clubApi.getClubEvents(clubId, eventType)

    suspend fun createClubEvent(
        clubId: Int,
        request: ClubEventRequest
    ) = clubAuthApi.createClubEvent(clubId, request)

    suspend fun getClubEvent(
        clubId: Int,
        eventId: Int
    ) = clubApi.getClubEvent(clubId, eventId)

    suspend fun modifyClubEvent(
        clubId: Int,
        eventId: Int,
        request: ClubEventRequest
    ) = clubAuthApi.modifyClubEvent(clubId, eventId, request)

    suspend fun deleteClubEvent(
        clubId: Int,
        eventId: Int
    ) = clubAuthApi.deleteClubEvent(clubId, eventId)
}
