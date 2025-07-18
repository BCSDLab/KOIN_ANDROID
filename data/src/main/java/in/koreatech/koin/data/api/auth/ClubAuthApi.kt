package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.club.ClubCreateRequest
import `in`.koreatech.koin.data.request.club.ClubEmpowermentRequest
import `in`.koreatech.koin.data.request.club.ClubEventRequest
import `in`.koreatech.koin.data.request.club.ClubModifyRequest
import `in`.koreatech.koin.data.request.club.ClubQnaRequest
import `in`.koreatech.koin.data.request.club.ClubRecruitmentRequest
import `in`.koreatech.koin.data.response.club.ClubDetailsResponse
import `in`.koreatech.koin.data.response.club.ClubEventResponse
import `in`.koreatech.koin.data.response.club.ClubsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ClubAuthApi {
    @GET(URLConstant.CLUBS.CLUBS)
    suspend fun getClubs(
        @Query("categoryId") id: Int?,
        @Query("sortType") sortType: String?,
        @Query("isRecruiting") isRecruiting: Boolean,
        @Query("query") query: String
    ): ClubsResponse

    @GET(URLConstant.CLUBS.CLUBID.CLUBID)
    suspend fun getClubDetails(
        @Path("clubId") clubId: Int
    ): ClubDetailsResponse

    @POST(URLConstant.CLUBS.CLUBS)
    suspend fun createClub(
        @Body request: ClubCreateRequest
    )

    @PUT(URLConstant.CLUBS.CLUBID.CLUBID)
    suspend fun modifyClub(
        @Path("clubId") clubId: Int,
        @Body request: ClubModifyRequest
    )

    @PUT(URLConstant.CLUBS.EMPOWERMENT)
    suspend fun setClubEmpowerment(
        @Body request: ClubEmpowermentRequest
    )

    @PUT(URLConstant.CLUBS.CLUBID.LIKE.LIKE)
    suspend fun setClubLike(
        @Path("clubId") clubId: Int
    )

    @POST(URLConstant.CLUBS.CLUBID.QNA.QNA)
    suspend fun postClubQna(
        @Path("clubId") clubId: Int,
        @Body request: ClubQnaRequest
    )

    @DELETE(URLConstant.CLUBS.CLUBID.QNA.QNAID)
    suspend fun deleteClubQna(
        @Path("clubId") clubId: Int,
        @Path("qnaId") qnaId: Int
    )

    @DELETE(URLConstant.CLUBS.CLUBID.LIKE.CANCEL)
    suspend fun cancelClubLike(
        @Path("clubId") clubId: Int
    )

    @POST(URLConstant.CLUBS.CLUBID.RECRUITMENT.RECRUITMENT)
    suspend fun createClubRecruitment(
        @Path("clubId") clubId: Int,
        @Body request: ClubRecruitmentRequest
    )

    @DELETE(URLConstant.CLUBS.CLUBID.RECRUITMENT.RECRUITMENT)
    suspend fun deleteClubRecruitment(
        @Path("clubId") clubId: Int
    ): Response<Unit>

    @PUT(URLConstant.CLUBS.CLUBID.RECRUITMENT.RECRUITMENT)
    suspend fun modifyClubRecruitment(
        @Path("clubId") clubId: Int,
        @Body request: ClubRecruitmentRequest
    ): Response<Unit>

    @GET(URLConstant.CLUBS.CLUBID.EVENT.EVENTS)
    suspend fun getClubEvents(
        @Path("clubId") clubId: Int,
        @Query("eventType") eventType: String
    ): List<ClubEventResponse>

    @POST(URLConstant.CLUBS.CLUBID.EVENT.EVENT)
    suspend fun createClubEvent(
        @Path("clubId") clubId: Int,
        @Body request: ClubEventRequest
    ): Response<Unit>

    @PUT(URLConstant.CLUBS.CLUBID.EVENT.EVENTID.EVENTID)
    suspend fun modifyClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int,
        @Body request: ClubEventRequest
    ): Response<Unit>

    @DELETE(URLConstant.CLUBS.CLUBID.EVENT.EVENTID.EVENTID)
    suspend fun deleteClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int
    ): Response<Unit>

    @POST(URLConstant.CLUBS.CLUBID.RECRUITMENT.NOTIFICATION)
    suspend fun subscribeClubRecruitment(
        @Path("clubId") clubId: Int
    ): Response<Unit>

    @DELETE(URLConstant.CLUBS.CLUBID.RECRUITMENT.NOTIFICATION)
    suspend fun unsubscribeClubRecruitment(
        @Path("clubId") clubId: Int
    ): Response<Unit>

    @POST(URLConstant.CLUBS.CLUBID.EVENT.EVENTID.NOTIFICATION)
    suspend fun subscribeClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int
    ): Response<Unit>

    @DELETE(URLConstant.CLUBS.CLUBID.EVENT.EVENTID.NOTIFICATION)
    suspend fun unsubscribeClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int
    ): Response<Unit>
}
