package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOrderableShopMenusUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(shopId: Int): Result<List<ShopMenus>> = storeRepository.getOrderableShopMenus(shopId)
}
