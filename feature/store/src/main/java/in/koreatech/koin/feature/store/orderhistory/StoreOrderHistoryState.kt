package `in`.koreatech.koin.feature.store.orderhistory

import `in`.koreatech.koin.feature.store.orderhistory.model.StoreOrderHistoryFilters

data class StoreOrderHistoryState(
    val isLoading: Boolean = false,
    val showFilters: Boolean = false,
    val filters: StoreOrderHistoryFilters = StoreOrderHistoryFilters(),
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val selectedIndex: Int = 0
)
