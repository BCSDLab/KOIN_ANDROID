package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.util.ext.sortedOpenStore
import `in`.koreatech.koin.domain.util.match
import javax.inject.Inject

class SearchStoresUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(
        search: String? = null,
    ): List<Store> {
        return storeRepository.getStores()
            .filter { if (search != null) it.name.match(search) else true }
            .sortedOpenStore()
    }
}
