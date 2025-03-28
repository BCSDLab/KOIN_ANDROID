package `in`.koreatech.koin.feature.banner.model

import `in`.koreatech.koin.domain.model.banner.Banner
import `in`.koreatech.koin.domain.model.banner.BannerCategory
import java.util.Locale

data class BannerState(
    val bannerList: List<LocalBanner> = emptyList(),
    val bannerCategory: List<BannerCategory> = emptyList(),
    val isLoading: Boolean = true,
    val currentVersionCode: Int = 0
)

data class LocalBanner(
    val id: Int = 0,
    val imageUrl: String = "",
    val redirectLink: String? = null,
    val version: Int = 0
)

fun Banner.toLocalBanner() = LocalBanner(
    id = id,
    imageUrl = imageUrl,
    redirectLink = redirectLink,
    version = version?.split(".")?.joinToString("") {
        String.format(Locale.KOREA, "%02d", it.toInt())
    }?.toInt() ?: 0
)
