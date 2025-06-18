package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.club.ClubCreateRequest
import `in`.koreatech.koin.data.request.club.ClubEmpowermentRequest
import `in`.koreatech.koin.data.request.club.ClubModifyRequest
import `in`.koreatech.koin.data.request.club.ClubQnaRequest
import `in`.koreatech.koin.data.response.club.ClubDetailsResponse
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
        @Query("sortType") sortType: String?
    ): ClubsResponse

    @GET("clubs/{clubId}")
    suspend fun getClubDetails(
        @Path("clubId") clubId: Int
    ): ClubDetailsResponse

    @POST(URLConstant.CLUBS.CLUBS)
    suspend fun createClub(
        @Body request: ClubCreateRequest
    )

    @PUT("${URLConstant.CLUBS.CLUBS}/{clubId}")
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
    )

    @DELETE("clubs/{clubId}/like/cancel")
    suspend fun cancelClubLike(
        @Path("clubId") clubId: Int
    )
}
