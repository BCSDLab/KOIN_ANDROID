package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.store.CartAddRequest
import `in`.koreatech.koin.data.request.store.CartItemRequest
import `in`.koreatech.koin.data.response.store.CartItemEditResponse
import `in`.koreatech.koin.data.response.store.CartPaymentSummaryResponse
import `in`.koreatech.koin.data.response.store.CartResponse
import `in`.koreatech.koin.data.response.store.CartSummaryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreAuthApi {
    @PUT("/cart/item/{cartMenuItemId}")
    suspend fun updateCartItem(
        @Path("cartMenuItemId") cartMenuItemId: Int,
        @Body cartItemRequest: CartItemRequest
    )

    @POST("/cart/quantity/{cartMenuItemId}/{quantity}")
    suspend fun updateCartItemQuantity(
        @Path("cartMenuItemId") cartMenuItemId: Int,
        @Path("quantity") quantity: Int
    )

    @POST("/cart/add")
    suspend fun addCartItem(
        @Body cartAddRequest: CartAddRequest
    )

    @GET("/cart")
    suspend fun getCartItems(
        @Query("type") type: String
    ): CartResponse

    @GET("/cart/validate")
    suspend fun validateCartItems()

    @GET("/cart/summary/{orderableShopId}")
    suspend fun getCartSummary(
        @Path("orderableShopId") orderableShopId: Int
    ): CartSummaryResponse

    @GET("/cart/payment/summary")
    suspend fun getCartPaymentSummary(
        @Query("type") type: String
    ): CartPaymentSummaryResponse

    @GET("cart/item/{cartMenuItemId}/edit")
    suspend fun getCartItemEdit(
        @Path("cartMenuItemId") cartMenuItemId: Int
    ): CartItemEditResponse

    @DELETE("/cart/reset")
    suspend fun resetCart(): Response<Unit>

    @DELETE("/cart/delete/{cartMenuItemId}")
    suspend fun deleteCartItem(
        @Path("cartMenuItemId") cartMenuItemId: Int
    ): Response<Unit>
}
