package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.util.ext.sortedOpenStore
import `in`.koreatech.koin.domain.util.match
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(
        category: StoreCategory? = null,
        storeSorter: StoreSorter? = StoreSorter.NONE,
        isOperating: Boolean? = null,
        isDelivery: Boolean? = null
    ): List<Store> {
        return storeRepository.getStores(
            storeSorter = storeSorter,
            isOperating = isOperating,
            isDelivery = isDelivery
        )
            .filter {
                category in it.categoryIds
            }
    }
}
