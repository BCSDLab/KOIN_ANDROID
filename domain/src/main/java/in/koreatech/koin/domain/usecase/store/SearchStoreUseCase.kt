package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class SearchStoreUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(
        search: String = "",
        category: StoreCategory? = null
    ): List<Store> {
        return storeRepository.getStores()
            .filter {
                if (search == "") category in it.categoryIds else it.name.contains(search)
            }
    }
}
