package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.cart.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getCart(): Flow<Cart>
}