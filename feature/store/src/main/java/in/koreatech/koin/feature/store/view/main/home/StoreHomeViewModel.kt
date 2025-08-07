package `in`.koreatech.koin.feature.store.view.main.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.store.GetCartItemsCountUseCase
import `in`.koreatech.koin.domain.usecase.store.GetOrderableShopsUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreCategoriesUseCase
import `in`.koreatech.koin.feature.store.enums.MinimumPriceOption
import `in`.koreatech.koin.feature.store.enums.OrderOption
import `in`.koreatech.koin.feature.store.enums.StoreFilter
import `in`.koreatech.koin.feature.store.enums.toStoreSorter
import `in`.koreatech.koin.feature.store.model.toLocalShop
import `in`.koreatech.koin.feature.store.model.toLocalStoreCategories
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class StoreHomeViewModel @Inject constructor(
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase,
    private val getCartItemsCountUseCase: GetCartItemsCountUseCase,
    private val getOrderableShopsUseCase: GetOrderableShopsUseCase
) : ViewModel(), ContainerHost<StoreHomeState, StoreHomeSideEffect> {
    override val container = container<StoreHomeState, StoreHomeSideEffect>(StoreHomeState())

    init {
        intent {
            getStoreCategoriesUseCase().let {
                reduce {
                    state.copy(
                        storeCategories = it.map { it.toLocalStoreCategories() }
                    )
                }
            }
        }
    }

    fun getCartItemsCount() = intent {
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

    fun fetchData() = intent {
        reduce {
            state.copy(
                isLoading = true
            )
        }
        getOrderableShopsUseCase(
            sorter = state.selectedOrderOption.toStoreSorter(),
            filter = state.selectedStoreFilter.map { it.name },
            categoryId = state.categoryId,
            minimumOrderAmount = state.selectedMinimumPriceOption.price
        ).onSuccess {
            reduce {
                state.copy(
                    orderableShops = it.map {
                        it.toLocalShop()
                    },
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
                }
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

    fun onCategoryChange(categoryId: Int) = intent {
        if (categoryId == state.categoryId) return@intent
        reduce {
            state.copy(
                categoryId = categoryId
            )
        }
    }
}
