package `in`.koreatech.koin.feature.store.orderhistory.model

import `in`.koreatech.koin.feature.store.enums.OrderInProgressStatus
import `in`.koreatech.koin.feature.store.orderhistory.enums.OrderType
import java.time.LocalTime

data class OrderInProgressData(
    val id: Int,
    val paymentId: Int,
    val orderType: OrderType,
    val orderableShopName: String,
    val orderableShopThumbnail: String,
    val estimatedAt: LocalTime?,
    val orderStatus: OrderInProgressStatus,
    val orderTitle: String,
    val totalAmount: Int
)
