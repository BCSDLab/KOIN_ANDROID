package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.ordershop.OrderMenuList
import `in`.koreatech.koin.domain.model.ordershop.OrderShop
import `in`.koreatech.koin.domain.model.ordershop.OrderShopSummary

interface OrderShopRepository {
    suspend fun getOrderableShopMenus(orderableShopId: Int): List<OrderMenuList>
    suspend fun getOrderableShopSummary(orderableShopId: Int): OrderShopSummary
    suspend fun getOrderableShopDetail(orderableShopId: Int): OrderShop
}
