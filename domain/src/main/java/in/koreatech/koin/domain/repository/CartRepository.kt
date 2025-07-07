package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.cart.Cart
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.model.cart.CartValidate
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getCart(type: CartType): Flow<Cart>
    suspend fun getCartValidate(): Flow<CartValidate>
    suspend fun cartMenuQuantity(cartMenuItemId: Int, quantity:Int): Flow<Unit>
    suspend fun resetCart(): Flow<Unit>
    suspend fun deleteCartMenuItem(cartMenuItemId: Int): Flow<Unit>
}