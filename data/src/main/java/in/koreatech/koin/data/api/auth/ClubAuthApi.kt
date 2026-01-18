package `in`.koreatech.koin.data.api.auth

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
    @GET("clubs")
    suspend fun getClubs(
        @Query("categoryId") id: Int?,
        @Query("sortType") sortType: String?,
        @Query("isRecruiting") isRecruiting: Boolean,
        @Query("query") query: String
    ): ClubsResponse

    @GET("clubs/{clubId}")
    suspend fun getClubDetails(
        @Path("clubId") clubId: Int
    ): ClubDetailsResponse

    @POST("clubs")
    suspend fun createClub(
        @Body request: ClubCreateRequest
    )

    @PUT("clubs/{clubId}")
    suspend fun modifyClub(
        @Path("clubId") clubId: Int,
        @Body request: ClubModifyRequest
    )

    @PUT("clubs/empowerment")
    suspend fun setClubEmpowerment(
        @Body request: ClubEmpowermentRequest
    )

    @PUT("clubs/{clubId}/like")
    suspend fun setClubLike(
        @Path("clubId") clubId: Int
    )

    @POST("clubs/{clubId}/qna")
    suspend fun postClubQna(
        @Path("clubId") clubId: Int,
        @Body request: ClubQnaRequest
    )

    @DELETE("clubs/{clubId}/qna/{qnaId}")
    suspend fun deleteClubQna(
        @Path("clubId") clubId: Int,
        @Path("qnaId") qnaId: Int
    ): Response<Unit>

    @DELETE("clubs/{clubId}/like/cancel")
    suspend fun cancelClubLike(
        @Path("clubId") clubId: Int
    )

    @POST("clubs/{clubId}/recruitment")
    suspend fun createClubRecruitment(
        @Path("clubId") clubId: Int,
        @Body request: ClubRecruitmentRequest
    )

    @DELETE("clubs/{clubId}/recruitment")
    suspend fun deleteClubRecruitment(
        @Path("clubId") clubId: Int
    ): Response<Unit>

    @PUT("clubs/{clubId}/recruitment")
    suspend fun modifyClubRecruitment(
        @Path("clubId") clubId: Int,
        @Body request: ClubRecruitmentRequest
    ): Response<Unit>

    @GET("clubs/{clubId}/events")
    suspend fun getClubEvents(
        @Path("clubId") clubId: Int,
        @Query("eventType") eventType: String
    ): List<ClubEventResponse>

    @POST("clubs/{clubId}/event")
    suspend fun createClubEvent(
        @Path("clubId") clubId: Int,
        @Body request: ClubEventRequest
    ): Response<Unit>

    @PUT("clubs/{clubId}/event/{eventId}")
    suspend fun modifyClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int,
        @Body request: ClubEventRequest
    ): Response<Unit>

    @DELETE("clubs/{clubId}/event/{eventId}")
    suspend fun deleteClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int
    ): Response<Unit>

    @POST("clubs/{clubId}/recruitment/notification")
    suspend fun subscribeClubRecruitment(
        @Path("clubId") clubId: Int
    ): Response<Unit>

    @DELETE("clubs/{clubId}/recruitment/notification")
    suspend fun unsubscribeClubRecruitment(
        @Path("clubId") clubId: Int
    ): Response<Unit>

    @POST("clubs/{clubId}/event/{eventId}/notification")
    suspend fun subscribeClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int
    ): Response<Unit>

    @DELETE("clubs/{clubId}/event/{eventId}/notification")
    suspend fun unsubscribeClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int
    ): Response<Unit>
}
