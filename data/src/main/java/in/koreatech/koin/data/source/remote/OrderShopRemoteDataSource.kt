package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.OrderShopApi
import javax.inject.Inject

class OrderShopRemoteDataSource @Inject constructor(
    private val orderShopApi: OrderShopApi,
) {
    suspend fun getOrderableShopMenus(orderableShopId: Int) =
        orderShopApi.getOrderableShopMenus(orderableShopId)

    suspend fun getOrderableShopSummary(orderableShopId: Int) =
        orderShopApi.getOrderableShopSummary(orderableShopId)

    suspend fun getOrderableShopDetail(orderableShopId: Int) =
        orderShopApi.getOrderableShopDetail(orderableShopId)
}