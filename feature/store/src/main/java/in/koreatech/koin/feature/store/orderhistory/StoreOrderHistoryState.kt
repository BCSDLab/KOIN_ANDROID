package `in`.koreatech.koin.feature.store.orderhistory

import `in`.koreatech.koin.feature.store.model.LocalOrderInProgress
import `in`.koreatech.koin.feature.store.orderhistory.model.OrderHistoryData
import `in`.koreatech.koin.feature.store.orderhistory.model.StoreOrderHistoryFilters

data class StoreOrderHistoryState(
    val isLoading: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val showFilters: Boolean = false,
    val filters: StoreOrderHistoryFilters = StoreOrderHistoryFilters(),
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val selectedIndex: Int = 0,
    val orderHistories: List<OrderHistoryData> = emptyList(),
    val orderInProgress: List<LocalOrderInProgress> = emptyList(),
    val isLoggedIn: Boolean = false,
    val currentPage: Int = 1,
    val totalPage: Int = Int.MAX_VALUE, // This is placeholder
    val showSignInDialog: Boolean = false,
    val cartItemCount: Int = 0
)
