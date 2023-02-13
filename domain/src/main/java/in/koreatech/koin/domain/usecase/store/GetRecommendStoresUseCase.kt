package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.constant.STORE_RECOMMEND_STORES
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.util.ext.isCurrentOpen
import `in`.koreatech.koin.domain.util.match
import javax.inject.Inject

class GetRecommendStoresUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        store: Store
    ): List<Store> {
        return storeRepository.getStores()
            .filter {
                store.category == it.category && it.isCurrentOpen && it != store
            }
            .shuffled()
            .take(STORE_RECOMMEND_STORES)
    }
}