package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.domain.model.store.OrderHistoryOrders
import `in`.koreatech.koin.feature.store.enums.OrderHistoryStatus
import `in`.koreatech.koin.feature.store.enums.StoreStatus
import java.text.SimpleDateFormat
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
    val formattedOrderDate = try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("M월 d일 (E)", Locale.KOREA)
        val date = inputFormat.parse(orderDate)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        orderDate
    }

    return OrderHistoryData(
        id = id,
        paymentId = paymentId,
        orderableShopId = orderableShopId,
        orderableShopName = orderableShopName,
        openStatus = StoreStatus.valueOf(openStatus),
        orderableShopThumbnail = orderableShopThumbnail,
        orderDate = formattedOrderDate,
        orderStatus = OrderHistoryStatus.valueOf(orderStatus),
        orderTitle = orderTitle,
        totalAmount = totalAmount
    )
}
