package `in`.koreatech.koin.feature.store.orderhistory

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.store.GetHistoryRelatedUseCase
import `in`.koreatech.koin.domain.usecase.store.GetOrderInProgressUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.store.model.toLocalOrderInProgress
import `in`.koreatech.koin.feature.store.orderhistory.model.StoreOrderHistoryFilters
import `in`.koreatech.koin.feature.store.orderhistory.model.toOrderHistoryData
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class StoreOrderHistoryViewModel @Inject constructor(
    private val getHistoryRelatedUseCase: GetHistoryRelatedUseCase,
    private val getOrderInProgressUseCase: GetOrderInProgressUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<StoreOrderHistoryState, StoreOrderHistorySideEffect> {
    override val container = container<StoreOrderHistoryState, StoreOrderHistorySideEffect>(StoreOrderHistoryState())

    init {
        getUserType()
    }

    fun getUserType() = intent {
        getUserStatusUseCase().collect {
            when (it) {
                is User.Student,
                is User.General -> {
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

    fun getOrderHistories() = intent {
        if (state.isLoadingNextPage || state.isLoading) return@intent
        reduce {
            state.copy(isLoading = true)
        }
        getHistoryRelatedUseCase(
            page = 1,
            period = state.filters.orderHistoryPeriod?.name ?: "NONE",
            status = state.filters.orderStatusFilter?.name ?: "NONE",
            type = state.filters.orderType?.name ?: "NONE",
            query = state.searchQuery
        ).onSuccess {
            if (state.currentPage > it.currentPage) return@intent
            reduce {
                state.copy(
                    isLoading = false,
                    orderHistories = it.orders.map { it.toOrderHistoryData() },
                    totalPage = Int.MAX_VALUE // Reset total page to Int.MAX_VALUE if filters or query change
                )
            }
        }.onFailure {
            reduce {
                state.copy(isLoading = false)
            }
        }
    }

    fun getMoreOrderHistories() = intent {
        if (state.isLoadingNextPage || state.isLoading) return@intent
        reduce {
            state.copy(isLoadingNextPage = true)
        }
        getHistoryRelatedUseCase(
            page = state.currentPage,
            period = state.filters.orderHistoryPeriod?.name ?: "NONE",
            status = state.filters.orderStatusFilter?.name ?: "NONE",
            type = state.filters.orderType?.name ?: "NONE",
            query = state.searchQuery
        ).onSuccess {
            reduce {
                state.copy(
                    orderHistories = state.orderHistories + it.orders.map { it.toOrderHistoryData() },
                    totalPage = it.totalPage,
                    currentPage = it.currentPage,
                    isLoadingNextPage = false
                )
            }
        }.onFailure {
            reduce {
                state.copy(isLoadingNextPage = false)
            }
        }
    }

    fun getOrderInProgress() = intent {
        getOrderInProgressUseCase().onSuccess {
            reduce {
                state.copy(
                    orderInProgress = it.map { it.toLocalOrderInProgress() }
                )
            }
        }
    }

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

    fun requestNextPage() = blockingIntent {
        if (state.currentPage >= state.totalPage || state.isLoadingNextPage || state.isLoading) return@blockingIntent
        reduce {
            state.copy(
                currentPage = state.currentPage + 1
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

    fun updateShowSignInDialog(showSignInDialog: Boolean) = blockingIntent {
        reduce {
            state.copy(
                showSignInDialog = showSignInDialog
            )
        }
    }
}
