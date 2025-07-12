package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.club.ClubCategoriesResponse
import `in`.koreatech.koin.data.response.club.ClubHotResponse
import `in`.koreatech.koin.data.response.club.ClubQnasResponse
import `in`.koreatech.koin.data.response.club.ClubRecruitmentResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ClubApi {
    @GET(URLConstant.CLUBS.CATEGORIES)
    suspend fun getClubsCategories(): ClubCategoriesResponse

    @GET(URLConstant.CLUBS.HOT)
    suspend fun getClubHot(): ClubHotResponse

    @GET(URLConstant.CLUBS.CLUBID.QNA.QNA)
    suspend fun getClubQnas(
        @Path("clubId") clubId: Int
    ): ClubQnasResponse

    @GET(URLConstant.CLUBS.CLUBID.RECRUITMENT.RECRUITMENT)
    suspend fun getClubRecruitment(
        @Path("clubId") clubId: Int
    ): ClubRecruitmentResponse
}
