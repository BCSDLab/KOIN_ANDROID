package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.club.ClubCategoriesResponse
import retrofit2.http.GET

interface ClubApi {
    @GET(URLConstant.CLUBS.CATEGORIES)
    suspend fun getClubsCategories(): ClubCategoriesResponse
}
