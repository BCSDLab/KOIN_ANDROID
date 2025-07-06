package `in`.koreatech.koin.domain.usecase.cart

import `in`.koreatech.koin.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartMenuQuantityUseCase  @Inject constructor(
    private val cartRepository: CartRepository
){
    operator fun invoke(cartMenuItemId: Int, quantity: Int): Flow<Unit> = flow {
        emitAll(cartRepository.cartMenuQuantity(cartMenuItemId, quantity))
    }
}
