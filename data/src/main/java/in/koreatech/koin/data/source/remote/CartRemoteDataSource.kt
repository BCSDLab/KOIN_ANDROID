package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.CartAuthApi
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val cartApi: CartAuthApi
) {
    suspend fun getCartValidate() = cartApi.getCartValidate()
    suspend fun getCartQuantityMenu(cartMenuItemId: Int, quantity: Int) =
        cartApi.getCartQuantityMenu(cartMenuItemId, quantity)
    suspend fun resetCart() = cartApi.resetCart()
    suspend fun deleteCartMenuItem(cartMenuItemId: Int) =
        cartApi.deleteCartMenuItem(cartMenuItemId)
}
