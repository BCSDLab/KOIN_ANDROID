package `in`.koreatech.koin.feature.dining.ui.diningdetail

import `in`.koreatech.koin.domain.model.dining.Dining
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DiningState(
    val selectedDate: String,
    val dining: ImmutableList<Dining> = persistentListOf(),
    val showBottomSheet: Boolean = false,
    val isSoldOutSubscribed: Boolean = false,
    val isDiningImageSubscribed: Boolean = false,
    val isDiningRefreshing: Boolean = false,
    val isLoading: Boolean = false
)
