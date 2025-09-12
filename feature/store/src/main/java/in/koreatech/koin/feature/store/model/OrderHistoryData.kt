package `in`.koreatech.koin.feature.store.model

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
    val orderDate: String,
    val orderStatus: OrderHistoryStatus,
    val orderTitle: String,
    val openStatus: StoreStatus,
    val orderableShopThumbnail: String,
    val totalAmount: Int
)

fun OrderHistoryOrders.toOrderHistoryData(): OrderHistoryData {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREA)
    val date = LocalDate.parse(orderDate, inputFormatter)
    val formattedOrderDate = date.format(outputFormatter)

    return OrderHistoryData(
        id = id,
        paymentId = paymentId,
        orderableShopId = orderableShopId,
        orderableShopName = orderableShopName,
        openStatus = if (openStatus) StoreStatus.OPEN else StoreStatus.PRE_OPEN, // API에 openStatus 구분이 추가되면 SOLD_OUT도 넣어야함
        orderableShopThumbnail = orderableShopThumbnail,
        orderDate = formattedOrderDate,
        orderStatus = OrderHistoryStatus.valueOf(orderStatus),
        orderTitle = orderTitle,
        totalAmount = totalAmount
    )
}
