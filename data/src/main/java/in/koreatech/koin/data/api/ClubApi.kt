package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.club.ClubCategoriesResponse
import `in`.koreatech.koin.data.response.club.ClubEventResponse
import `in`.koreatech.koin.data.response.club.ClubHotResponse
import `in`.koreatech.koin.data.response.club.ClubQnasResponse
import `in`.koreatech.koin.data.response.club.ClubRecruitmentResponse
import `in`.koreatech.koin.data.response.club.ClubSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ClubApi {
    @GET("clubs/categories")
    suspend fun getClubsCategories(): ClubCategoriesResponse

    @GET("clubs/hot")
    suspend fun getClubHot(): ClubHotResponse

    @GET("clubs/{clubId}/qna")
    suspend fun getClubQnas(
        @Path("clubId") clubId: Int
    ): ClubQnasResponse

    @GET("clubs/{clubId}/recruitment")
    suspend fun getClubRecruitment(
        @Path("clubId") clubId: Int
    ): ClubRecruitmentResponse

    @GET("clubs/{clubId}/event/{eventId}")
    suspend fun getClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int
    ): ClubEventResponse

    @GET("clubs/search/related")
    suspend fun searchClubs(
        @Query("query") query: String
    ): ClubSearchResponse
}
