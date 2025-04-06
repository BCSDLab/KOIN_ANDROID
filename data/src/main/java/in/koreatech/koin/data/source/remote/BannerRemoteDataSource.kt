package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.BannerApi
import `in`.koreatech.koin.data.response.banner.BannerCategoryListResponse
import `in`.koreatech.koin.data.response.banner.BannerListResponse
import javax.inject.Inject

class BannerRemoteDataSource @Inject constructor(
    private val bannerApi: BannerApi
) {
    suspend fun getBannersByCategory(
        categoryId: Int,
        platform: String
    ): BannerListResponse {
        return bannerApi.getBannersByCategory(categoryId, platform)
    }

    suspend fun getBannerCategories(): BannerCategoryListResponse {
        return bannerApi.getBannerCategories()
    }
}
