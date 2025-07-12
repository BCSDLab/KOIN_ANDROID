package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class UpdateCartItemQuantityUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        cartMenuItemId: Int,
        quantity: Int
    ): Result<Unit> = storeRepository.updateCartItemQuantity(cartMenuItemId, quantity)
}
