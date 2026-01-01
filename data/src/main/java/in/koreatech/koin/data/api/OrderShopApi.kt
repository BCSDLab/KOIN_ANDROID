package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.ordershop.OrderMenuListResponse
import `in`.koreatech.koin.data.ordershop.OrderShopResponse
import `in`.koreatech.koin.data.ordershop.ShopOriginResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderShopApi {
    @GET("order/shop/{orderableShopId}/menus")
    suspend fun getOrderableShopMenus(
        @Path("orderableShopId") orderableShopId: Int
    ): List<OrderMenuListResponse>

    @GET("order/shop/{orderableShopId}/summary")
    suspend fun getOrderableShopSummary(
        @Path("orderableShopId") orderableShopId: Int
    ): OrderShopResponse

    @GET("order/shop/{orderableShopId}/detail")
    suspend fun getOrderableShopDetail(
        @Path("orderableShopId") orderableShopId: Int
    ): ShopOriginResponse
}
