package `in`.koreatech.koin.domain.usecase.banner

import `in`.koreatech.koin.domain.model.banner.BannerCategory
import `in`.koreatech.koin.domain.repository.BannerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetBannerCategoriesUseCase @Inject constructor(
    private val bannerRepository: BannerRepository
) {
    suspend operator fun invoke(): Flow<List<BannerCategory>> =
        bannerRepository.getBannerCategories()
}
