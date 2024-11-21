package `in`.koreatech.koin.domain.usecase.store.search

import `in`.koreatech.koin.domain.model.store.ShopSearchRelated
import `in`.koreatech.koin.domain.model.store.ShopSearchRelatedList
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetRelatedStoreUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(query: String): ShopSearchRelatedList {
        return try {
            storeRepository.getShopSearchRelatedList(query)
        }
        catch (e: Exception) {
            ShopSearchRelatedList(emptyList())
        }
    }
}