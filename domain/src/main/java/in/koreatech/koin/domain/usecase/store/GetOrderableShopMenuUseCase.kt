package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.ShopMenu
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOrderableShopMenuUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        shopId: Int,
        menuId: Int
    ): Result<ShopMenu> = storeRepository.getOrderableShopMenu(shopId, menuId)
}
