package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.feature.store.component.BannerCategory

data class LocalStoreBanner(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val category: BannerCategory
)