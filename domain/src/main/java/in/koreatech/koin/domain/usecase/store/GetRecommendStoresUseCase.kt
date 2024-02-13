package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.constant.STORE_RECOMMEND_STORES
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetRecommendStoresUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        store: StoreWithMenu
    ): List<Store> {
        return storeRepository.getStores()
            .filter {
                val shopRandomCategoryId = store.shopCategories?.filter { it.id != StoreCategory.All.code }?.randomOrNull()?.id
                it.categoryIds.find {
                    it?.code == shopRandomCategoryId
                }?.code == shopRandomCategoryId && !it.open.closed && it.uid != store.uid
            }
            .shuffled()
            .take(STORE_RECOMMEND_STORES)
    }
}