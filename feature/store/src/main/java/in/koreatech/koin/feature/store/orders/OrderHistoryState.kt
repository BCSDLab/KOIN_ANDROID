package `in`.koreatech.koin.feature.store.orders

import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderFilter
import `in`.koreatech.koin.feature.store.model.OrderHistoryData
import `in`.koreatech.koin.feature.store.model.OrderInProgressData

data class OrderHistoryState(
    val page: Int = 1,
    val totalPage: Int = 2,
    val filters: OrderFilter = OrderFilter(
        period = PeriodOption.NONE,
        type = TypeOption.NONE,
        status = StatusOption.NONE
    ),
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = true,
    val orderHistories: List<OrderHistoryData> = emptyList(),
    val orderInProgress: List<OrderInProgressData> = emptyList(),
    val isSearching: Boolean = false,
    val isFilterSelecting: Boolean = false,
    val searchQuery: String = "",
    val cartItemCount: Int = 0,
    val isLoggedIn: Boolean = false,
    val showSignInDialog: Boolean = false
)
