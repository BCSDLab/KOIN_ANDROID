package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.response.cart.CartResponse
import `in`.koreatech.koin.data.response.cart.CartValidateResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartAuthApi {
    @GET("cart")
    suspend fun getCart(): CartResponse
    @GET("cart/validate")
    suspend fun getCartValidate(): CartValidateResponse
    @POST("cart/quantity/{cartMenuItemId}/{quantity}")
    suspend fun getCartQuantityMenu(
        @Path("cartMenuItemId") cartMenuItemId: Int,
        @Path("quantity") quantity: Int
    ): Response<Unit>
    @DELETE("cart/reset")
    suspend fun resetCart(): Response<Unit>
    @DELETE("cart/delete/{cartMenuItemId}")
    suspend fun deleteCartMenuItem(
        @Path("cartMenuItemId") cartMenuItemId: Int
    ): Response<Unit>

}