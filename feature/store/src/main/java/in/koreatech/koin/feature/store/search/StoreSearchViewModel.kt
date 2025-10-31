package `in`.koreatech.koin.feature.store.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.store.GetOrderableShopSearchRelatedUseCase
import `in`.koreatech.koin.domain.usecase.store.search.GetRelatedStoreUseCase
import `in`.koreatech.koin.feature.store.model.toLocalShopSearchResult
import `in`.koreatech.koin.feature.store.navigation.IS_ORDERABLE_SHOP
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class StoreSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderableShopSearchRelatedUseCase: GetOrderableShopSearchRelatedUseCase,
    private val getRelatedStoreUseCase: GetRelatedStoreUseCase
) : ViewModel(), ContainerHost<StoreSearchState, StoreSearchSideEffect> {
    override val container = container<StoreSearchState, StoreSearchSideEffect>(StoreSearchState()) {
        val isOrderableShop = savedStateHandle.get<Boolean>(IS_ORDERABLE_SHOP) ?: true

        blockingIntent {
            reduce {
                state.copy(
                    isOrderableShop = isOrderableShop
                )
            }
        }
    }

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
        if (state.isOrderableShop) {
            getOrderableShopSearchRelatedUseCase(
                query = state.searchQuery
            ).onSuccess {
                reduce {
                    state.copy(
                        searchResults = it.shopNameSearchResults.map { it.toLocalShopSearchResult() } + it.menuNameSearchResults.map { it.toLocalShopSearchResult() }
                    )
                }
            }
        } else {
            getRelatedStoreUseCase(
                query = state.searchQuery
            ).let {
                reduce {
                    state.copy(
                        searchResults = it.keywords.map { it.toLocalShopSearchResult() }
                    )
                }
            }
        }
    }
}
