package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.util.match
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        category: StoreCategory? = null,
        search: String? = null
    ): List<Store> {
        return storeRepository.getStores()
            .filter {
                if(category == null) return@filter true
                category in it.categoryIds
            }
            .filter { if (search != null) it.name.match(search) else true }
    }
}