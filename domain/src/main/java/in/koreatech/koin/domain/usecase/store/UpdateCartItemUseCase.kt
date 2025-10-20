package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.CartItem
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class UpdateCartItemUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        cartMenuItemId: Int,
        cartItem: CartItem
    ): Result<Unit> = storeRepository.updateCartItem(cartMenuItemId, cartItem)
}
