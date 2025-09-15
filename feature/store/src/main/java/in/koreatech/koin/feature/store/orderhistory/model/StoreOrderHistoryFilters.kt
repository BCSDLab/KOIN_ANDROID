package `in`.koreatech.koin.feature.store.orderhistory.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.store.orderhistory.enums.OrderHistoryPeriod
import `in`.koreatech.koin.feature.store.orderhistory.enums.OrderStatusFilter
import `in`.koreatech.koin.feature.store.model.OrderType

@Immutable
data class StoreOrderHistoryFilters(
    val orderHistoryPeriod: OrderHistoryPeriod? = null,
    val orderStatusFilter: OrderStatusFilter? = null,
    val orderType: OrderType? = null
)
