package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.ShopSummary
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOrderableShopSummaryUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(shopId: Int): Result<ShopSummary> = storeRepository.getOrderableShopSummary(shopId)
}
