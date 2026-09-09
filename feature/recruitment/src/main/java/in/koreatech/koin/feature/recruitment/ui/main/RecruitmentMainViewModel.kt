package `in`.koreatech.koin.feature.recruitment.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.recruitment.GetRecruitmentNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.GetRecruitmentsUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentSort
import `in`.koreatech.koin.feature.recruitment.ui.main.model.toRecruitmentItemModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.subIntent
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
@Suppress("TooManyFunctions")
class RecruitmentMainViewModel @Inject constructor(
    private val getRecruitmentsUseCase: GetRecruitmentsUseCase,
    private val getRecruitmentNotificationsUseCase: GetRecruitmentNotificationsUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(),
    ContainerHost<RecruitmentMainState, RecruitmentMainSideEffect> {

    override val container = container<RecruitmentMainState, RecruitmentMainSideEffect>(
        RecruitmentMainState()
    ) {
        observeUserStatus()
    }

    private var searchJob: Job? = null
    private val loadMoreMutex = Mutex()

    init {
        fetchRecruitments()
    }

    fun fetchRecruitments() = intent {
        fetchRecruitmentsSub()
    }

    fun observeUserStatus() = intent {
        getUserStatusUseCase().distinctUntilChanged().collect { user ->
            val isLoggedIn = user !is User.Anonymous
            reduce { state.copy(isLoggedIn = isLoggedIn) }
            if (isLoggedIn) {
                getNotificationCount()
            } else {
                reduce { state.copy(isUnreadNotificationAvailable = false) }
            }
        }
    }

    fun getNotificationCount() = intent {
        if (!state.isLoggedIn) return@intent
        getRecruitmentNotificationsUseCase(page = 1, limit = 1).onSuccess {
            if (it.unreadCount == 0) {
                reduce { state.copy(isUnreadNotificationAvailable = false) }
            } else {
                reduce { state.copy(isUnreadNotificationAvailable = true) }
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun fetchRecruitmentsSub() = subIntent {
        reduce {
            state.copy(isRefreshing = true)
        }
        val filter = state.filterState
        getRecruitmentsUseCase(
            keyword = state.searchValue.takeIf { it.isNotBlank() },
            status = filter.selectedStatus?.apiValue,
            categories = filter.selectedCategories
                .takeIf { it.isNotEmpty() }
                ?.map { it.apiValue },
            meetingType = filter.selectedLocation?.apiValue,
            sort = filter.selectedSort.apiValue,
            page = 1,
            limit = RECRUITMENTS_PAGE_SIZE
        ).onSuccess { recruitments ->
            reduce {
                state.copy(
                    items = recruitments.recruitments
                        .map { it.toRecruitmentItemModel() }
                        .toImmutableList(),
                    totalCount = recruitments.totalCount,
                    currentPage = recruitments.currentPage,
                    totalPage = recruitments.totalPage,
                    isRefreshing = false
                )
            }
        }.onFailure {
            reduce { state.copy(isRefreshing = false) }
            postSideEffect(RecruitmentMainSideEffect.ShowError)
        }
    }

    fun onWriteClick() = intent {
        if (state.isLoggedIn) {
            postSideEffect(RecruitmentMainSideEffect.NavigateToWrite)
        } else {
            reduce { state.copy(isLoginRequiredDialogVisible = true) }
        }
    }

    fun onNotificationClick() = intent {
        if (state.isLoggedIn) {
            postSideEffect(RecruitmentMainSideEffect.NavigateToNotification)
        } else {
            reduce { state.copy(isLoginRequiredDialogVisible = true) }
        }
    }

    fun dismissLoginRequiredDialog() = blockingIntent {
        reduce { state.copy(isLoginRequiredDialogVisible = false) }
    }

    fun confirmLoginRequiredDialog() = intent {
        reduce { state.copy(isLoginRequiredDialogVisible = false) }
        postSideEffect(RecruitmentMainSideEffect.NavigateToLogin)
    }

    fun loadMoreRecruitments() = intent {
        if (!loadMoreMutex.tryLock()) return@intent
        try {
            if (state.currentPage >= state.totalPage) return@intent
            reduce { state.copy(isLoadingMore = true) }
            val filter = state.filterState
            getRecruitmentsUseCase(
                keyword = state.searchValue.takeIf { it.isNotBlank() },
                status = filter.selectedStatus?.apiValue,
                categories = filter.selectedCategories
                    .takeIf { it.isNotEmpty() }
                    ?.map { it.apiValue },
                meetingType = filter.selectedLocation?.apiValue,
                sort = filter.selectedSort.apiValue,
                page = state.currentPage + 1,
                limit = RECRUITMENTS_PAGE_SIZE
            ).onSuccess { recruitments ->
                reduce {
                    state.copy(
                        items = (
                            state.items + recruitments.recruitments.map { it.toRecruitmentItemModel() }
                            ).distinctBy { it.id }.toImmutableList(),
                        totalCount = recruitments.totalCount,
                        currentPage = recruitments.currentPage,
                        totalPage = recruitments.totalPage,
                        isLoadingMore = false
                    )
                }
            }.onFailure {
                reduce { state.copy(isLoadingMore = false) }
                postSideEffect(RecruitmentMainSideEffect.ShowError)
            }
        } finally {
            loadMoreMutex.unlock()
        }
    }

    fun updateSearch(query: String) = blockingIntent {
        reduce { state.copy(searchValue = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            fetchRecruitments()
        }
    }

    fun updateFilterVisible(visible: Boolean) = blockingIntent {
        reduce {
            if (visible) {
                state.copy(isFilterVisible = true, pendingFilterState = state.filterState)
            } else {
                state.copy(isFilterVisible = false)
            }
        }
    }

    fun selectPendingStatus(status: RecruitmentStatus?) =
        updatePendingFilter { it.copy(selectedStatus = status) }

    fun selectPendingSort(sort: RecruitmentSort) =
        updatePendingFilter { it.copy(selectedSort = sort) }

    fun togglePendingCategory(category: RecruitmentCategory?) =
        updatePendingFilter { it.copy(selectedCategories = it.selectedCategories.toggle(category)) }

    fun selectPendingLocation(location: RecruitmentLocation?) =
        updatePendingFilter { it.copy(selectedLocation = location) }

    fun resetPendingFilter() = updatePendingFilter { RecruitmentFilterState() }

    fun applyPendingFilter() = intent {
        reduce { state.copy(filterState = state.pendingFilterState, isFilterVisible = false) }
        fetchRecruitmentsSub()
    }

    fun removeStatusFilter() = updateFilter { it.copy(selectedStatus = null) }

    fun removeCategoryFilter(category: RecruitmentCategory) =
        updateFilter { it.copy(selectedCategories = (it.selectedCategories - category).toPersistentList()) }

    fun removeLocationFilter() = updateFilter { it.copy(selectedLocation = null) }

    private fun updatePendingFilter(transform: (RecruitmentFilterState) -> RecruitmentFilterState) =
        blockingIntent {
            reduce { state.copy(pendingFilterState = transform(state.pendingFilterState)) }
        }

    private fun updateFilter(transform: (RecruitmentFilterState) -> RecruitmentFilterState) =
        intent {
            reduce { state.copy(filterState = transform(state.filterState)) }
            fetchRecruitmentsSub()
        }

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 300L
        private const val RECRUITMENTS_PAGE_SIZE = 10
    }
}

private fun <T> ImmutableList<T>.toggle(item: T?): ImmutableList<T> = when {
    item == null -> persistentListOf()
    item in this -> (this - item).toPersistentList()
    else -> (this + item).toPersistentList()
}
