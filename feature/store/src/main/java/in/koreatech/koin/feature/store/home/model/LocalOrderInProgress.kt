package `in`.koreatech.koin.feature.store.home.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.model.store.OrderInProgress
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Immutable
data class LocalOrderInProgress(
    val type: CartType,
    val orderStatus: OrderStatus,
    val estimatedAt: LocalTime?,
    val shopName: String,
    val paymentId: Int
)

fun OrderInProgress.toLocalOrderInProgress(): LocalOrderInProgress {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    return LocalOrderInProgress(
        type = CartType.valueOf(orderType),
        orderStatus = OrderStatus.fromString(orderStatus),
        estimatedAt = if (estimatedAt != null) LocalTime.parse(estimatedAt, formatter) else null,
        shopName = orderableShopName,
        paymentId = paymentId
    )
}
