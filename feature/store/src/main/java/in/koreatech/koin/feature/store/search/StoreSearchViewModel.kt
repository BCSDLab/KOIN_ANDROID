package `in`.koreatech.koin.feature.store.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.store.GetOrderableShopSearchRelatedUseCase
import `in`.koreatech.koin.feature.store.model.toLocalShopSearchResult
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class StoreSearchViewModel @Inject constructor(
    private val getOrderableShopSearchRelatedUseCase: GetOrderableShopSearchRelatedUseCase
) : ViewModel(), ContainerHost<StoreSearchState, StoreSearchSideEffect> {
    override val container = container<StoreSearchState, StoreSearchSideEffect>(StoreSearchState())

    fun updateSearchQuery(query: String) = blockingIntent {
        reduce {
            state.copy(
                searchQuery = query
            )
        }
    }

    fun onSearch() = intent {
        if (state.searchQuery.isEmpty()) {
            reduce {
                state.copy(
                    searchResults = emptyList()
                )
            }
            return@intent
        }
        getOrderableShopSearchRelatedUseCase(
            query = state.searchQuery
        ).onSuccess {
            reduce {
                state.copy(
                    searchResults = it.shopNameSearchResults.map { it.toLocalShopSearchResult() } +
                        it.menuNameSearchResults.map { it.toLocalShopSearchResult() }
                )
            }
        }
    }
}
