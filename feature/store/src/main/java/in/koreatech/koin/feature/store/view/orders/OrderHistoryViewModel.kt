package `in`.koreatech.koin.feature.store.view.orders

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.store.GetCartItemsCountUseCase
import `in`.koreatech.koin.domain.usecase.store.GetHistoryRelatedUseCase
import `in`.koreatech.koin.domain.usecase.store.GetOnGoingRelatedUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderFilter
import `in`.koreatech.koin.feature.store.model.toOrderHistoryData
import `in`.koreatech.koin.feature.store.model.toOrderOnGoingData
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getHistoryRelatedUseCase: GetHistoryRelatedUseCase,
    private val getOnGoingRelatedUseCase: GetOnGoingRelatedUseCase,
    private val getCartItemsCountUseCase: GetCartItemsCountUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<OrderHistoryState, OrderHistorySideEffect> {
    override val container = container<OrderHistoryState, OrderHistorySideEffect>(OrderHistoryState())
    private var page = 0
    private var totalPage = 1

    init {
        getOrderHistoryData()
        // getOrderOnGoingData()
    }

    fun getNewOrderHistoryData() {
        page = 0
        totalPage = 1
        getOrderHistoryData()
    }

    fun getOrderHistoryData() {
        if (page < totalPage) {
            page += 1
            intent {
                getHistoryRelatedUseCase(
                    page = page,
                    limit = 10,
                    period = state.filters.period.name,
                    status = state.filters.status.name,
                    type = state.filters.type.name,
                    query = state.searchQuery
                ).onSuccess { data ->
                    totalPage = data.totalPage
                    reduce {
                        state.copy(
                            orderHistories = state.orderHistories + data.orders.map { it.toOrderHistoryData() }
                        )
                    }
                }.onFailure {
                }
            }
        }
    }

    private fun getOrderOnGoingData() {
        intent {
            getOnGoingRelatedUseCase().let { data ->
                reduce {
                    state.copy(
                        orderOnGoings = data.map { it.toOrderOnGoingData() }
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
                    // Do nothing
                    reduce {
                        state.copy(isLoggedIn = false)
                    }
                }
            }
        }
    }

    private fun getCartItemsCount() = intent {
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
            postSideEffect(OrderHistorySideEffect.NavigateToCart)
        } else {
            reduce {
                state.copy(showSignInDialog = true)
            }
        }
    }

    fun hideSignInDialog() = intent {
        reduce { state.copy(showSignInDialog = false) }
    }

    fun onSearchQueryChanged(query: String) = intent {
        reduce {
            state.copy(
                searchQuery = query
            )
        }
    }
    fun openFilterOverlay() = intent {
        reduce {
            state.copy(
                isFilterSelecting = true,
                isTyping = false
            )
        }
    }

    fun applyFilterOverlay(newFilters: OrderFilter) {
        intent {
            reduce {
                state.copy(
                    filters = newFilters
                )
            }
        }
        getNewOrderHistoryData()
    }

    fun closeFilterOverlay() = intent {
        reduce {
            state.copy(
                isFilterSelecting = false
            )
        }
    }

    fun resetFilter() {
        intent {
            reduce {
                state.copy(
                    filters = OrderFilter(
                        period = PeriodOption.NONE,
                        type = TypeOption.NONE,
                        status = StatusOption.NONE
                    )
                )
            }
        }
    }

    fun onSearchStart() {
        intent {
            reduce {
                state.copy(isTyping = true)
            }
        }
    }

    fun onSearchCancel() {
        intent {
            reduce {
                state.copy(
                    isTyping = false,
                    searchQuery = ""
                )
            }
        }
    }

    fun typingEnd() {
        intent {
            reduce {
                state.copy(isTyping = false)
            }
        }
        getNewOrderHistoryData()
    }

    fun onTabSelected(selectedTabIndex: Int) {
        intent {
            reduce {
                state.copy(
                    selectedTabIndex = selectedTabIndex,
                    isTyping = false
                )
            }
        }
    }
}
