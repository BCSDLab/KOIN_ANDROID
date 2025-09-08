package `in`.koreatech.koin.feature.store.view.orders

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.store.GetCartItemsCountUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.store.enums.LocationOption
import `in`.koreatech.koin.feature.store.enums.OrderStatus
import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.StoreStatus
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderFilter
import `in`.koreatech.koin.feature.store.model.OrderHistoryData
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    // private val getOrderHistoriesUseCase: GetOrderHistoriesUseCase,
    // private val getOrderOnGoingsUseCase: GetOrderOnGoingsUseCase,
    private val getCartItemsCountUseCase: GetCartItemsCountUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<OrderHistoryState, OrderHistorySideEffect> {
    override val container = container<OrderHistoryState, OrderHistorySideEffect>(OrderHistoryState())

    init {
        intent {
            /*
            getOrderHistoriesUseCase().let {
                reduce {
                    state.copy(
                        orderHistories = it.map { it.toOrderHistoryDatas() }
                    )
                }
            }
            getOrderOnGoingsUseCase().let {
                reduce {
                    state.copy(
                        orderOnGoings = it.map { it.toOrderOnGoinfDatas() }
                    )
                }
            }
             */
            reduce {
                state.copy(
                    orderHistories = listOf(
                        OrderHistoryData(
                            id = 1,
                            paymentId = 1,
                            orderableShopId = 1,
                            orderStatus = OrderStatus.CANCELLED,
                            orderDate = "9월 5일 (금)",
                            storeImageUrl = "https://example.com/store_thumbnail.jpg",
                            storeStatus = StoreStatus.SOLD_OUT,
                            orderableShopName = "맛있는 족발 - 병천점",
                            orderTitle = "족발 + 막국수 저녁 set 외 1건",
                            price = 32500
                        ),
                        OrderHistoryData(
                            id = 1,
                            paymentId = 1,
                            orderableShopId = 1,
                            orderStatus = OrderStatus.DELIVERED,
                            orderDate = "9월 5일 (금)",
                            storeImageUrl = "https://example.com/store_thumbnail.jpg",
                            storeStatus = StoreStatus.SOLD_OUT,
                            orderableShopName = "맛있는 족발 - 병천점",
                            orderTitle = "족발 + 막국수 저녁 set 외 1건",
                            price = 32500
                        )
                    ),
                    orderOnGoings = listOf(
                        /*
                        OrderOnGoingData(
                            TypeOption.TAKEOUT,
                            time = "오후 8:32",
                            storeImageUrl = "https://example.com/store_thumbnail.jpg",
                            storeName = "맛있는 족발 - 병천점",
                            orders = "족발 + 막국수 저녁 set 외 1건",
                            price = 32500
                        ),
                        OrderOnGoingData(
                            TypeOption.DELIVERY,
                            time = "오후 8:32",
                            storeImageUrl = "https://example.com/store_thumbnail.jpg",
                            storeName = "맛있는 족발 - 병천점",
                            orders = "족발 + 막국수 저녁 set 외 1건",
                            price = 32500
                        )
                         */
                    )
                )
            }

            // Todo : 주문 내역 가져오기 API USE CASE
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

    fun applyFilterOverlay(newFilters: OrderFilter) = intent {
        reduce {
            state.copy(
                filters = newFilters
            )
            // TODO : 필터로 검색 ㄱㄱ
        }
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
                        location = LocationOption.DEFAULT,
                        period = PeriodOption.DEFAULT,
                        type = TypeOption.DEFAULT,
                        status = StatusOption.DEFAULT
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

    fun typingCancel() {
        intent {
            reduce {
                state.copy(isTyping = false)
            }
        }
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
