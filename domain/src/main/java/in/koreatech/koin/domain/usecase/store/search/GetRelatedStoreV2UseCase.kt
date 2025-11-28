package `in`.koreatech.koin.domain.usecase.store.search

import `in`.koreatech.koin.domain.model.store.OrderableShopSearchRelated
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetRelatedStoreV2UseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        query: String
    ): Result<OrderableShopSearchRelated> = storeRepository.getShopSearchRelatedListV2(query)
}
