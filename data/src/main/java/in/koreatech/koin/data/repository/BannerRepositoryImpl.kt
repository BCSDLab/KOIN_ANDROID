package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toListOfBanner
import `in`.koreatech.koin.data.mapper.toListOfBannerCategory
import `in`.koreatech.koin.data.source.remote.BannerRemoteDataSource
import `in`.koreatech.koin.domain.model.banner.Banner
import `in`.koreatech.koin.domain.model.banner.BannerCategory
import `in`.koreatech.koin.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val bannerRemoteDataSource: BannerRemoteDataSource
): BannerRepository {
    override suspend fun getBannersByCategory(
        categoryId: Int,
        platform: String
    ): Flow<List<Banner>> {
        return flow {
            emit(
                bannerRemoteDataSource.getBannersByCategory(categoryId, platform).banners.toListOfBanner()
            )
        }
    }

    override suspend fun getBannerCategories(): Flow<List<BannerCategory>> {
        return flow {
            emit(
                bannerRemoteDataSource.getBannerCategories().categories.toListOfBannerCategory()
            )
        }
    }

}