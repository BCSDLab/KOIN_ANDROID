package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.domain.model.store.OrderOnGoingRelated
import `in`.koreatech.koin.feature.store.enums.OrderOnGoingStatus
import `in`.koreatech.koin.feature.store.enums.TypeOption
import java.text.SimpleDateFormat
import java.util.Locale

data class OrderOnGoingData(
    val id: Int,
    val paymentId: Int,
    val orderType: TypeOption,
    val orderableShopName: String,
    val orderableShopThumbnail: String,
    val estimatedAt: String,
    val orderStatus: OrderOnGoingStatus,
    val orderTitle: String,
    val totalAmount: Int
)

fun OrderOnGoingRelated.toOrderOnGoingData(): OrderOnGoingData {
    val formattedEstimatedAt = try {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("a h:mm", Locale.KOREA)
        val date = inputFormat.parse(estimatedAt)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        estimatedAt
    }

    return OrderOnGoingData(
        id = id,
        paymentId = paymentId,
        orderType = TypeOption.valueOf(orderType),
        orderableShopName = orderableShopName,
        orderableShopThumbnail = orderableShopThumbnail,
        estimatedAt = formattedEstimatedAt,
        orderStatus = OrderOnGoingStatus.valueOf(orderStatus),
        orderTitle = orderTitle,
        totalAmount = totalAmount
    )
}
