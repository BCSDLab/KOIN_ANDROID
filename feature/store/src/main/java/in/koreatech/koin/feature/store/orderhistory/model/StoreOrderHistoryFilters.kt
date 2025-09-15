package `in`.koreatech.koin.feature.store.orderhistory.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.store.orderhistory.enums.OrderHistoryPeriod
import `in`.koreatech.koin.feature.store.orderhistory.enums.OrderStatus
import `in`.koreatech.koin.feature.store.orderhistory.enums.OrderType

@Immutable
data class StoreOrderHistoryFilters(
    val orderHistoryPeriod: OrderHistoryPeriod? = null,
    val orderStatus: OrderStatus? = null,
    val orderType: OrderType? = null
)
