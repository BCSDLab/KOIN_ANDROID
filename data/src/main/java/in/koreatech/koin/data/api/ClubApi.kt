package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
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
    @GET(URLConstant.CLUBS.CATEGORIES)
    suspend fun getClubsCategories(): ClubCategoriesResponse

    @GET(URLConstant.CLUBS.HOT)
    suspend fun getClubHot(): ClubHotResponse

    @GET("clubs/{clubId}/qna")
    suspend fun getClubQnas(
        @Path("clubId") clubId: Int
    ): ClubQnasResponse

    @GET(URLConstant.CLUBS.CLUBID.RECRUITMENT.RECRUITMENT)
    suspend fun getClubRecruitment(
        @Path("clubId") clubId: Int
    ): ClubRecruitmentResponse

    @GET(URLConstant.CLUBS.CLUBID.EVENT.EVENTID.EVENTID)
    suspend fun getClubEvent(
        @Path("clubId") clubId: Int,
        @Path("eventId") eventId: Int
    ): ClubEventResponse

    @GET(URLConstant.CLUBS.SEARCH)
    suspend fun searchClubs(
        @Query("query") query: String
    ): ClubSearchResponse
}
