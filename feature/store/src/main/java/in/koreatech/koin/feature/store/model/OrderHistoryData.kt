package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.feature.store.enums.OrderStatus
import `in`.koreatech.koin.feature.store.enums.StoreStatus

data class OrderHistoryData(
    val status: OrderStatus,
    val date: String,
    val storeImageUrl: String,
    val storeStatus: StoreStatus,
    val storeName: String,
    val orders: String,
    val price: Int
)
