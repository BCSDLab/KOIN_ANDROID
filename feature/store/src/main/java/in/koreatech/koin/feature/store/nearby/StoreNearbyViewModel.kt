package `in`.koreatech.koin.feature.store.nearby

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.developer.DeveloperOption
import `in`.koreatech.koin.core.developer.DeveloperOptionUtil
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.store.GetCartItemsCountUseCase
import `in`.koreatech.koin.domain.usecase.store.GetNearbyShopUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreCategoriesUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.store.enums.MinimumPriceOption
import `in`.koreatech.koin.feature.store.enums.OrderOption
import `in`.koreatech.koin.feature.store.enums.StoreFilter
import `in`.koreatech.koin.feature.store.enums.toStoreSorter
import `in`.koreatech.koin.feature.store.model.toLocalShop
import `in`.koreatech.koin.feature.store.model.toLocalStoreCategories
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class StoreNearbyViewModel @Inject constructor(
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase,
    private val getCartItemsCountUseCase: GetCartItemsCountUseCase,
    private val getNearbyShopUseCase: GetNearbyShopUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<StoreNearbyState, StoreNearbySideEffect> {
    override val container = container<StoreNearbyState, StoreNearbySideEffect>(StoreNearbyState())

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

    fun getUserType() = intent {
        getUserStatusUseCase().collect {
            when (it) {
                is User.Student,
                is User.General -> {
                    getCartItemsCount()
                    reduce {
                        state.copy(isLoggedIn = true)
                    }
                }
                is User.Anonymous -> {
                    reduce {
                        state.copy(isLoggedIn = false)
                    }
                }
            }
        }
    }

    private fun getCartItemsCount() = intent {
        if (!DeveloperOptionUtil.getDeveloperOption(DeveloperOption.DeliverySprint)) return@intent
        reduce {
            state.copy(isLoading = true)
        }
        getCartItemsCountUseCase().onSuccess { count ->
            reduce {
                state.copy(cartItemCount = count.totalQuantity, isLoading = false)
            }
        }.onFailure {
            reduce {
                state.copy(isLoading = false)
            }
        }
    }

    fun navigateToCart() = intent {
        if (state.isLoggedIn) {
            postSideEffect(StoreNearbySideEffect.NavigateToCart)
        } else {
            reduce {
                state.copy(showSignInDialog = true)
            }
        }
    }

    fun hideSignInDialog() = intent {
        reduce { state.copy(showSignInDialog = false) }
    }

    fun fetchData() = intent {
        reduce {
            state.copy(
                isLoading = true
            )
        }
        getNearbyShopUseCase(
            categoryId = state.categoryId,
            sorter = state.selectedOrderOption.toStoreSorter(),
            isOperating = StoreFilter.IS_OPEN in state.selectedStoreFilter
        ).onSuccess {
            reduce {
                state.copy(
                    orderableShops = it.map { it.toLocalShop() }.toImmutableList(),
                    isLoading = false
                )
            }
        }.onFailure {
            reduce {
                state.copy(
                    isLoading = false
                )
            }
        }
    }

    fun onShowOrderOptionsChange(showOrderOptions: Boolean) = intent {
        reduce {
            state.copy(
                showOrderOptions = showOrderOptions
            )
        }
    }

    fun onSelectedOrderOptionChange(orderOption: OrderOption) = intent {
        reduce {
            state.copy(
                selectedOrderOption = orderOption
            )
        }
    }

    fun onSelectedStoreFilterChange(selectedStoreFilter: StoreFilter) = intent {
        reduce {
            state.copy(
                selectedStoreFilter = if (state.selectedStoreFilter.contains(selectedStoreFilter)) {
                    state.selectedStoreFilter - selectedStoreFilter
                } else {
                    state.selectedStoreFilter + selectedStoreFilter
                }.toImmutableList()
            )
        }
    }

    fun onShowMinimumPriceOptionsChange(showMinimumPriceOptions: Boolean) = intent {
        reduce {
            state.copy(
                showMinimumPriceOptions = showMinimumPriceOptions
            )
        }
    }

    fun onSelectedMinimumPriceOptionChange(selectedMinimumPriceOption: MinimumPriceOption) = intent {
        reduce {
            state.copy(
                selectedMinimumPriceOption = selectedMinimumPriceOption
            )
        }
    }

    fun onShowSearchChange(showSearch: Boolean) = intent {
        reduce {
            state.copy(
                showSearch = showSearch
            )
        }
    }

    fun onCategoryChange(categoryId: Int) = intent {
        if (categoryId == state.categoryId) return@intent
        reduce {
            state.copy(
                categoryId = categoryId
            )
        }
    }

    fun onQueryChange(query: String) = blockingIntent {
        reduce {
            state.copy(
                query = query
            )
        }
    }

    fun onSearch() = intent {
        // TODO
    }
}
