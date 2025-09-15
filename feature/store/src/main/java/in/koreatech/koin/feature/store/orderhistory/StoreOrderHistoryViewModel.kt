package `in`.koreatech.koin.feature.store.orderhistory

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.store.orderhistory.model.StoreOrderHistoryFilters
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class StoreOrderHistoryViewModel @Inject constructor() : ViewModel(), ContainerHost<StoreOrderHistoryState, StoreOrderHistorySideEffect> {
    override val container = container<StoreOrderHistoryState, StoreOrderHistorySideEffect>(StoreOrderHistoryState())

    fun updateShowFilters(showFilters: Boolean) = intent {
        reduce {
            state.copy(
                showFilters = showFilters
            )
        }
    }

    fun updateFilters(filters: StoreOrderHistoryFilters) = intent {
        reduce {
            state.copy(
                filters = filters
            )
        }
    }

    fun updateIsSearching(isSearching: Boolean) = blockingIntent {
        reduce {
            state.copy(
                isSearching = isSearching
            )
        }
    }

    fun updateSearchQuery(searchQuery: String) = blockingIntent {
        reduce {
            state.copy(
                searchQuery = searchQuery
            )
        }
    }

    fun updateSelectedTab(selectedIndex: Int) = blockingIntent {
        reduce {
            state.copy(
                selectedIndex = selectedIndex
            )
        }
    }
}
