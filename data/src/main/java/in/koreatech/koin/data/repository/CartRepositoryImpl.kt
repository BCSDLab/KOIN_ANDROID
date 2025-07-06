package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.CartRemoteDataSource
import `in`.koreatech.koin.domain.model.cart.Cart
import `in`.koreatech.koin.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartRemoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun getCart(): Flow<Cart> {
        return flow {
            emit(cartRemoteDataSource.getCart().toCart())
        }
    }
}
