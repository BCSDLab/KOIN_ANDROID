package `in`.koreatech.koin.feature.banner.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.banner.Banner
import `in`.koreatech.koin.domain.model.banner.BannerCategory
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class BannerState(
    val bannerList: ImmutableList<LocalBanner> = persistentListOf(),
    val bannerCategory: ImmutableList<BannerCategory> = persistentListOf(),
    val isLoading: Boolean = true,
    val currentVersionCode: Int = 0
)

data class LocalBanner(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val redirectLink: String? = null,
    val version: Int = 0
)

fun Banner.toLocalBanner() = LocalBanner(
    id = id,
    title = title,
    imageUrl = imageUrl,
    redirectLink = redirectLink,
    version = if (version.isNullOrEmpty()) {
        0
    } else {
        try {
            version?.split(".")?.joinToString("") {
                String.format(Locale.KOREA, "%02d", it.toInt())
            }?.toInt() ?: 0
        } catch (e: NumberFormatException) {
            0
        }
    }
)
