package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.CartItemEdit
import `in`.koreatech.koin.domain.model.store.ShopMenu
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOrderableShopMenuUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        shopId: Int,
        menuId: Int
    ): Result<CartItemEdit> = storeRepository.getOrderableShopMenu(shopId, menuId)
}
