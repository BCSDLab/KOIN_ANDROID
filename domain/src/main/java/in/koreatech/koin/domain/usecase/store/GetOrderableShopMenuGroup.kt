package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.ShopMenusGroup
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOrderableShopMenuGroup @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(shopId: Int): Result<List<ShopMenusGroup>> = storeRepository.getOrderableShopMenuGroups(shopId)
}
