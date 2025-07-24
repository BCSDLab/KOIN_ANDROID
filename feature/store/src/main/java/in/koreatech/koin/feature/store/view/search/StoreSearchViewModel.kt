package `in`.koreatech.koin.feature.store.view.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class StoreSearchViewModel @Inject constructor() : ViewModel(), ContainerHost<StoreSearchState, StoreSearchSideEffect> {
    override val container = container<StoreSearchState, StoreSearchSideEffect>(StoreSearchState())

    fun updateSearchQuery(query: String) = blockingIntent {
        reduce {
            state.copy(
                searchQuery = query
            )
        }
    }

    fun onSearch() = intent {
        // TODO
    }
}
