package `in`.koreatech.koin.domain.usecase.cart

import `in`.koreatech.koin.domain.model.cart.Cart
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.repository.CartRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(type: CartType): Flow<Cart> {
        return cartRepository.getCart(type)
    }
}
