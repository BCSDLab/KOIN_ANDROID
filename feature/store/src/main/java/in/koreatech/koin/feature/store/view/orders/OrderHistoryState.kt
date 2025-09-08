package `in`.koreatech.koin.feature.store.view.orders

import `in`.koreatech.koin.feature.store.enums.LocationOption
import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderFilter
import `in`.koreatech.koin.feature.store.model.OrderHistoryData
import `in`.koreatech.koin.feature.store.model.OrderOnGoingData

data class OrderHistoryState(
    val filters: OrderFilter = OrderFilter(
        location = LocationOption.DEFAULT,
        period = PeriodOption.DEFAULT,
        type = TypeOption.DEFAULT,
        status = StatusOption.DEFAULT
    ),
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = true,
    val orderHistories: List<OrderHistoryData> = emptyList(),
    val orderOnGoings: List<OrderOnGoingData> = emptyList(),
    val isTyping: Boolean = false,
    val isFilterSelecting: Boolean = false,
    val searchQuery: String = "",
    val cartItemCount: Int = 0,
    val isLoggedIn: Boolean = false,
    val showSignInDialog: Boolean = false
)
