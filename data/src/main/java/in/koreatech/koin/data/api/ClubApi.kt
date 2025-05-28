package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.club.ClubCategoriesResponse
import `in`.koreatech.koin.data.response.club.ClubQnasResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ClubApi {
    @GET(URLConstant.CLUBS.CATEGORIES)
    suspend fun getClubsCategories(): ClubCategoriesResponse

    @GET("clubs/{clubId}/qna")
    suspend fun getClubQnas(
        @Path("clubId") clubId: Int
    ): ClubQnasResponse
}
