package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.CartAuthApi
import `in`.koreatech.koin.data.response.cart.CartResponse
import `in`.koreatech.koin.domain.model.cart.CartType
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val cartApi: CartAuthApi,
) {
    suspend fun getCart(type: CartType): CartResponse {
        return cartApi.getCart(type.name)
    }
    suspend fun getCartValidate() = cartApi.getCartValidate()
    suspend fun getCartQuantityMenu(cartMenuItemId: Int, quantity: Int) =
        cartApi.getCartQuantityMenu(cartMenuItemId, quantity)
    suspend fun resetCart() = cartApi.resetCart()
    suspend fun deleteCartMenuItem(cartMenuItemId: Int) =
        cartApi.deleteCartMenuItem(cartMenuItemId)
}