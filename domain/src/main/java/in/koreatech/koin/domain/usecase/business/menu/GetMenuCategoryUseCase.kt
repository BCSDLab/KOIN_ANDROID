package `in`.koreatech.koin.domain.usecase.business.menu

import `in`.koreatech.koin.domain.model.owner.StoreMenuCategory
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetMenuCategoryUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(
        storeId: Int
    ): List<StoreMenuCategory> {
        return storeRepository.getStoreMenuCategory(storeId)
    }
}