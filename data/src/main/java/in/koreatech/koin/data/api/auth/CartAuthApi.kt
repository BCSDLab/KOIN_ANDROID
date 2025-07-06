package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.response.cart.CartResponse
import `in`.koreatech.koin.data.response.cart.CartValidateResponse
import retrofit2.http.GET

interface CartAuthApi {
    @GET("cart")
    suspend fun getCart(): CartResponse
    @GET("cart/validate")
    suspend fun getCartValidate(): CartValidateResponse
}