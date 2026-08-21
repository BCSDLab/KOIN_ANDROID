package `in`.koreatech.koin.feature.dining.ui.diningdetail

import `in`.koreatech.koin.domain.model.dining.Dining

data class DiningState(
    val selectedDate: String,
    val dining: List<Dining> = emptyList(),
    val showBottomSheet: Boolean = false,
    val isSoldOutSubscribed: Boolean = false,
    val isDiningImageSubscribed: Boolean = false,
    val isDiningRefreshing: Boolean = false,
    val isLoading: Boolean = false
)
