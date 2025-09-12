package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.domain.model.store.OrderInProgress
import `in`.koreatech.koin.feature.store.enums.OrderInProgressStatus
import `in`.koreatech.koin.feature.store.enums.TypeOption
import java.text.SimpleDateFormat
import java.util.Locale

data class OrderInProgressData(
    val id: Int,
    val paymentId: Int,
    val orderType: TypeOption,
    val orderableShopName: String,
    val orderableShopThumbnail: String,
    val estimatedAt: String,
    val orderStatus: OrderInProgressStatus,
    val orderTitle: String,
    val totalAmount: Int
)

fun OrderInProgress.toOrderInProgressData(): OrderInProgressData {
    val formattedEstimatedAt = if (estimatedAt != null) {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("a h:mm", Locale.KOREA)
        val date = inputFormat.parse(estimatedAt!!)
        outputFormat.format(date!!)
    } else {
        ""
    }

    return OrderInProgressData(
        id = id,
        paymentId = paymentId,
        orderType = TypeOption.valueOf(orderType),
        orderableShopName = orderableShopName,
        orderableShopThumbnail = orderableShopThumbnail,
        estimatedAt = formattedEstimatedAt,
        orderStatus = OrderInProgressStatus.valueOf(orderStatus),
        orderTitle = orderTitle,
        totalAmount = totalAmount
    )
}
