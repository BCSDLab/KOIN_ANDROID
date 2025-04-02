package `in`.koreatech.koin.domain.usecase.banner

import `in`.koreatech.koin.domain.model.banner.Banner
import `in`.koreatech.koin.domain.repository.BannerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetBannersByCategoryUseCase @Inject constructor(
    private val bannerRepository: BannerRepository
) {
    suspend operator fun invoke(
        categoryId: Int,
        platform: String = "ANDROID"
    ): Flow<List<Banner>> = bannerRepository.getBannersByCategory(categoryId, platform)
}
