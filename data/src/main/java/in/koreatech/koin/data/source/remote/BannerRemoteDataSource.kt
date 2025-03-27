package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.BannerAuthApi
import `in`.koreatech.koin.data.response.banner.BannerCategoryListResponse
import `in`.koreatech.koin.data.response.banner.BannerListResponse
import javax.inject.Inject

class BannerRemoteDataSource @Inject constructor(
    private val bannerAuthApi: BannerAuthApi
) {
    suspend fun getBannersByCategory(
        categoryId: Int,
        platform: String
    ): BannerListResponse {
        return bannerAuthApi.getBannersByCategory(categoryId, platform)
    }

    suspend fun getBannerCategories(): BannerCategoryListResponse {
        return bannerAuthApi.getBannerCategories()
    }
}
