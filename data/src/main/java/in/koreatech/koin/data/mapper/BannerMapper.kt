package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.banner.BannerCategoryListResponse
import `in`.koreatech.koin.data.response.banner.BannerListResponse
import `in`.koreatech.koin.domain.model.banner.Banner
import `in`.koreatech.koin.domain.model.banner.BannerCategory

fun BannerListResponse.BannerResponse.toBanner() = Banner(
    id,
    title,
    imageUrl,
    redirectLink,
    version
)

fun List<BannerListResponse.BannerResponse>.toListOfBanner(): List<Banner> {
    return map { it.toBanner() }
}

fun BannerCategoryListResponse.BannerCategory.toBannerCategory() = BannerCategory(
    id,
    name
)

fun List<BannerCategoryListResponse.BannerCategory>.toListOfBannerCategory(): List<BannerCategory> {
    return map { it.toBannerCategory() }
}
