package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.feature.store.component.StoreStatus
import `in`.koreatech.koin.feature.store.enums.OrderStatus

data class OrderData(
    val status: OrderStatus,
    val date: String,
    val storeImageUrl: String,
    val storeStatus: StoreStatus,
    val storeName: String,
    val orders: String,
    val price: Int
)
