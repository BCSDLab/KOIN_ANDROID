package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.response.banner.BannerCategoryListResponse
import `in`.koreatech.koin.data.response.banner.BannerListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BannerAuthApi {
    @GET("banners/{categoryId}")
    suspend fun getBannersByCategory(
        @Path("categoryId") categoryId: Int,
        @Query("platform") platform: String
    ): BannerListResponse

    @GET("banner-categories")
    suspend fun getBannerCategories(): BannerCategoryListResponse
}
