package `in`.koreatech.koin.domain.usecase.cart

import `in`.koreatech.koin.domain.repository.CartRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CartMenuQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartMenuItemId: Int, quantity: Int): Flow<Unit> = cartRepository.cartMenuQuantity(cartMenuItemId, quantity)
}
