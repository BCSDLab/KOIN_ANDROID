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
                if(category == StoreCategory.Etc) { // 기타 + 콜벤 카테고리
                    it.category in arrayOf(null, StoreCategory.Etc, StoreCategory.Callvan)
                }

                if(category == null) return@filter true

                category == it.category
            }
            .filter { if (search != null) it.name.match(search) else true }
    }
}