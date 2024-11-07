package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(
        category: StoreCategories? = null,
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
                category?.id in it.categoryIds
            }
    }
}
