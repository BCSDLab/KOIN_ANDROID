package `in`.koreatech.koin.domain.usecase.cart

import `in`.koreatech.koin.domain.model.cart.Cart
import `in`.koreatech.koin.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Flow<Cart> {
        return cartRepository.getCart()
    }
}
