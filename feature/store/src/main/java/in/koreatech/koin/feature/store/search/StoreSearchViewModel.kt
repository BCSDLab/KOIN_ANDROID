package `in`.koreatech.koin.feature.store.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.store.GetOrderableShopSearchRelatedUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreCategoriesUseCase
import `in`.koreatech.koin.domain.usecase.store.search.GetRelatedStoreV2UseCase
import `in`.koreatech.koin.feature.store.model.toLocalShopSearchResult
import `in`.koreatech.koin.feature.store.model.toLocalStoreCategories
import `in`.koreatech.koin.feature.store.navigation.IS_ORDERABLE_SHOP
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class StoreSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderableShopSearchRelatedUseCase: GetOrderableShopSearchRelatedUseCase,
    private val getRelatedStoreV2UseCase: GetRelatedStoreV2UseCase,
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase
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

    init {
        intent {
            getStoreCategoriesUseCase().let {
                reduce {
                    state.copy(
                        storeCategories = it.map { it.toLocalStoreCategories() }.toImmutableList()
                    )
                }
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
            }.onFailure {
                Timber.e(it)
            }
        } else {
            getRelatedStoreV2UseCase(
                query = state.searchQuery
            ).onSuccess {
                reduce {
                    state.copy(
                        searchResults = it.shopNameSearchResults.map { it.toLocalShopSearchResult() } + it.menuNameSearchResults.map { it.toLocalShopSearchResult() }
                    )
                }
            }.onFailure {
                Timber.e(it)
            }
        }
    }
}
