package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.domain.model.store.OrderInProgress
import `in`.koreatech.koin.feature.store.enums.OrderInProgressStatus
import `in`.koreatech.koin.feature.store.enums.TypeOption
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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
        val inputFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREA)
        val time = LocalTime.parse(estimatedAt, inputFormatter)
        time.format(outputFormatter)
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
