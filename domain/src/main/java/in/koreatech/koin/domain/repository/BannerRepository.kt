package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.banner.Banner
import `in`.koreatech.koin.domain.model.banner.BannerCategory
import kotlinx.coroutines.flow.Flow

interface BannerRepository {
    suspend fun getBannersByCategory(
        categoryId: Int,
        platform: String
    ): Flow<List<Banner>>

    suspend fun getBannerCategories(): Flow<List<BannerCategory>>
}