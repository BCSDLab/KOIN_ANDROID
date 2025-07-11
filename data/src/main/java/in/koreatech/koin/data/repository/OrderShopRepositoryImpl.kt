package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toOrderMenuList
import `in`.koreatech.koin.data.mapper.toOrderShop
import `in`.koreatech.koin.data.mapper.toShopOrigin
import `in`.koreatech.koin.data.source.remote.OrderShopRemoteDataSource
import `in`.koreatech.koin.domain.model.ordershop.OrderMenuList
import `in`.koreatech.koin.domain.model.ordershop.OrderShopSummary
import `in`.koreatech.koin.domain.model.ordershop.OrderShop
import `in`.koreatech.koin.domain.repository.OrderShopRepository
import javax.inject.Inject

class OrderShopRepositoryImpl @Inject constructor(
    private val orderShopRemoteDataSource: OrderShopRemoteDataSource,
) : OrderShopRepository {
    override suspend fun getOrderableShopMenus(orderableShopId: Int): List<OrderMenuList> {
        return orderShopRemoteDataSource.getOrderableShopMenus(orderableShopId).map { it.toOrderMenuList() }
    }

    override suspend fun getOrderableShopSummary(orderableShopId: Int): OrderShopSummary {
        return orderShopRemoteDataSource.getOrderableShopSummary(orderableShopId).toOrderShop()
    }

    override suspend fun getOrderableShopDetail(orderableShopId: Int): OrderShop {
        return orderShopRemoteDataSource.getOrderableShopDetail(orderableShopId).toShopOrigin()
    }

}