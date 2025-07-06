package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.CartAuthApi
import `in`.koreatech.koin.data.response.cart.CartResponse
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val cartApi: CartAuthApi,
) {
    suspend fun getCart(): CartResponse {
        return cartApi.getCart()
    }
}