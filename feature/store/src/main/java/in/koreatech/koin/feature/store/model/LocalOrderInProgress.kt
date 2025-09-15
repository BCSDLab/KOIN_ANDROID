package `in`.koreatech.koin.feature.store.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.store.OrderInProgress
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Immutable
data class LocalOrderInProgress(
    val id: Int,
    val paymentId: Int,
    val orderType: OrderType,
    val shopName: String,
    val orderableShopThumbnail: String,
    val estimatedAt: LocalTime?,
    val orderStatus: OrderStatus,
    val orderTitle: String,
    val totalAmount: Int
)

fun OrderInProgress.toLocalOrderInProgress(): LocalOrderInProgress {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    return LocalOrderInProgress(
        id = id,
        paymentId = paymentId,
        orderType = OrderType.valueOf(orderType),
        shopName = orderableShopName,
        orderableShopThumbnail = orderableShopThumbnail,
        orderStatus = OrderStatus.fromString(orderStatus),
        estimatedAt = if (estimatedAt != null) LocalTime.parse(estimatedAt, formatter) else null,
        totalAmount = totalAmount,
        orderTitle = orderTitle
    )
}
