package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.banner.BannerCategoryListResponse
import `in`.koreatech.koin.data.response.banner.BannerListResponse
import `in`.koreatech.koin.domain.model.banner.Banner
import `in`.koreatech.koin.domain.model.banner.BannerCategory

fun BannerListResponse.BannerResponse.toBanner() = Banner (
    id ?: 0,
    imageUrl ?: "",
    redirectLink ?: "",
    version ?: ""
)

fun List<BannerListResponse.BannerResponse>.toListOfBanner(): List<Banner> {
    val result = mutableListOf<Banner>()
    this.map { result.add(it.toBanner()) }
    return result
}

fun BannerCategoryListResponse.BannerCategory.toBannerCategory() = BannerCategory (
    id ?: 0,
    name ?: ""
)

fun List<BannerCategoryListResponse.BannerCategory>.toListOfBannerCategory(): List<BannerCategory> {
    val result = mutableListOf<BannerCategory>()
    this.map { result.add(it.toBannerCategory()) }
    return result
}