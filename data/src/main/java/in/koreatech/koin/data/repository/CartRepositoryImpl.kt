package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.CartRemoteDataSource
import `in`.koreatech.koin.domain.model.cart.Cart
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartRemoteDataSource: CartRemoteDataSource
) : CartRepository {
    override suspend fun getCart(type:CartType): Flow<Cart> {
        return flow {
            emit(cartRemoteDataSource.getCart(type).toCart())
        }
    }
    override suspend fun getCartValidate() = flow {
        emit(cartRemoteDataSource.getCartValidate().toCartValidate())
    }

    override suspend fun cartMenuQuantity(
        cartMenuItemId: Int,
        quantity: Int
    ) = flow {
        val response = cartRemoteDataSource.getCartQuantityMenu(cartMenuItemId, quantity)
        if (response.isSuccessful) {
            emit(Unit)
        } else {
            throw HttpException(response)
        }
    }
    override suspend fun resetCart() = flow {
        val response = cartRemoteDataSource.resetCart()
        if (response.isSuccessful) {
            emit(Unit)
        } else {
            throw HttpException(response)
        }
    }
    override suspend fun deleteCartMenuItem(cartMenuItemId: Int) = flow {
        val response = cartRemoteDataSource.deleteCartMenuItem(cartMenuItemId)
        if (response.isSuccessful) {
            emit(Unit)
        } else {
            throw HttpException(response)
        }
    }
}
