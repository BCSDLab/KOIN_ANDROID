package `in`.koreatech.koin.feature.store.orderhistory.model

import `in`.koreatech.koin.domain.model.store.OrderHistoryOrders
import `in`.koreatech.koin.feature.store.enums.OrderHistoryStatus
import `in`.koreatech.koin.feature.store.enums.StoreStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class OrderHistoryData(
    val id: Int,
    val paymentId: Int,
    val orderableShopId: Int,
    val orderableShopName: String,
    val orderDate: LocalDate,
    val orderStatus: OrderHistoryStatus,
    val orderTitle: String,
    val openStatus: StoreStatus,
    val orderableShopThumbnail: String,
    val totalAmount: Int
)

fun OrderHistoryOrders.toOrderHistoryData(): OrderHistoryData {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.getDefault())
    val orderDate = LocalDate.parse(orderDate, inputFormatter)

    return OrderHistoryData(
        id = id,
        paymentId = paymentId,
        orderableShopId = orderableShopId,
        orderableShopName = orderableShopName,
        openStatus = if (openStatus) StoreStatus.OPEN else StoreStatus.PRE_OPEN, // API에 openStatus 구분이 추가되면 SOLD_OUT도 넣어야함
        orderableShopThumbnail = orderableShopThumbnail,
        orderDate = orderDate,
        orderStatus = OrderHistoryStatus.valueOf(orderStatus),
        orderTitle = orderTitle,
        totalAmount = totalAmount
    )
}
