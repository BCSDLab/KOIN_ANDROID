package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.repository.StoreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SearchStoreUseCase constructor(
    private val storeRepository: StoreRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        search: String = "",
        category: StoreCategory? = null,
        storeSorter: StoreSorter? = null,
        isOperating: Boolean? = null,
        isDelivery: Boolean? = null
    ): List<Store> {
        return withContext(coroutineDispatcher) {

            storeRepository.getStores(
                storeSorter = storeSorter,
                isOperating = isOperating,
                isDelivery = isDelivery
            )
                .filter {
                    if (search == "") category in it.categoryIds else it.name.contains(search)
                }
        }
    }
}
