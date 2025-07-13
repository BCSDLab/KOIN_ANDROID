package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.banner.BannerCategoryListResponse
import `in`.koreatech.koin.data.response.banner.BannerListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BannerApi {
    @GET(URLConstant.BANNER.CATEGORYID)
    suspend fun getBannersByCategory(
        @Path("categoryId") categoryId: Int,
        @Query("platform") platform: String
    ): BannerListResponse

    @GET(URLConstant.BANNER.CATEGORYS)
    suspend fun getBannerCategories(): BannerCategoryListResponse
}
