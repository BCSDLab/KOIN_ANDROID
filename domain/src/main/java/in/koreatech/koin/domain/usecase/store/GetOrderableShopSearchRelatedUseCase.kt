package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.OrderableShopSearchRelated
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOrderableShopSearchRelatedUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        query: String,
    ): Result<OrderableShopSearchRelated> = storeRepository.getOrderableShopSearchRelated(query)
}
