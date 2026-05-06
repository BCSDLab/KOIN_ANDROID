package `in`.koreatech.koin.feature.callvan.ui.list
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.callvan.CloseCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.CompleteCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanPostsUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanRestrictionUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.callvan.JoinCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.LeaveCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.ReopenCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationPermissionInfoUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.feature.callvan.model.toUiState
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanConfirmType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.ArrivalsFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.DeparturesFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.ListType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.StatusesType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListErrorType
import `in`.koreatech.koin.feature.callvan.ui.list.model.FilterBottomSheetState
import `in`.koreatech.koin.feature.callvan.ui.list.model.toListErrorType
import `in`.koreatech.koin.feature.callvan.ui.list.model.toUiState
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
@Suppress("LongParameterList", "TooManyFunctions")
class CallvanListViewModel @Inject constructor(
    private val getCallvanPostsUseCase: GetCallvanPostsUseCase,
    private val getCallvanRestrictionUseCase: GetCallvanRestrictionUseCase,
    private val joinCallvanPostUseCase: JoinCallvanPostUseCase,
    private val leaveCallvanPostUseCase: LeaveCallvanPostUseCase,
    private val closeCallvanPostUseCase: CloseCallvanPostUseCase,
    private val reopenCallvanPostUseCase: ReopenCallvanPostUseCase,
    private val completeCallvanPostUseCase: CompleteCallvanPostUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val onboardingManager: OnboardingManager,
    private val getNotificationPermissionInfoUseCase: GetNotificationPermissionInfoUseCase,
    private val updateNotificationSubscriptionUseCase: UpdateNotificationSubscriptionUseCase
) : ViewModel(), ContainerHost<CallvanListState, CallvanListSideEffect> {

    override val container = container<CallvanListState, CallvanListSideEffect>(
        CallvanListState()
    )

    init {
        initUserInfo()
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() = intent {
        container.stateFlow
            .map { it.searchValue }
            .distinctUntilChanged()
            .debounce(SEARCH_DEBOUNCE_MS)
            .collectLatest { fetchPosts() }
    }

    private fun initUserInfo() = intent {
        getUserStatusUseCase().collectLatest { user ->
            reduce {
                state.copy(isLoggedIn = user !is User.Anonymous)
            }
        }
    }

    fun updateBanDialogVisible(visible: Boolean) = blockingIntent {
        reduce { state.copy(showBanDialog = visible) }
    }

    fun fetchRestriction() = intent {
        if (!state.isLoggedIn) return@intent
        getCallvanRestrictionUseCase()
            .onSuccess { restriction ->
                val uiState = restriction.toUiState()
                reduce {
                    state.copy(
                        restriction = uiState,
                        showBanDialog = uiState.isRestricted
                    )
                }
            }
    }

    internal fun fetchHasNewNotification() = intent {
        if (state.isLoggedIn) {
            getNotificationsUseCase()
                .onSuccess { notifications ->
                    reduce {
                        state.copy(hasNewNotification = notifications.any { !it.isRead })
                    }
                }
        } else {
            reduce {
                state.copy(hasNewNotification = false)
            }
        }
    }

    fun updateSearch(query: String) = blockingIntent {
        reduce { state.copy(searchValue = query) }
    }

    fun join(postId: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        if (state.restriction.isRestricted) {
            reduce { state.copy(showBanDialog = true) }
            return@intent
        }
        joinCallvanPostUseCase(postId)
            .onSuccess { fetchPosts(state.items.size.coerceAtLeast(PAGE_SIZE)) }
            .onFailure { postSideEffect(CallvanListSideEffect.ShowSnackbar(it.toListErrorType())) }
    }

    fun cancelJoin(postId: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        leaveCallvanPostUseCase(postId)
            .onSuccess { fetchPosts(state.items.size.coerceAtLeast(PAGE_SIZE)) }
            .onFailure { postSideEffect(CallvanListSideEffect.ShowSnackbar(it.toListErrorType())) }
    }

    fun close(postId: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        closeCallvanPostUseCase(postId)
            .onSuccess { fetchPosts(state.items.size.coerceAtLeast(PAGE_SIZE)) }
            .onFailure { postSideEffect(CallvanListSideEffect.ShowSnackbar(it.toListErrorType())) }
    }

    fun reRecruit(postId: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        reopenCallvanPostUseCase(postId)
            .onSuccess { fetchPosts(state.items.size.coerceAtLeast(PAGE_SIZE)) }
            .onFailure { postSideEffect(CallvanListSideEffect.ShowSnackbar(it.toListErrorType())) }
    }

    fun complete(postId: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        completeCallvanPostUseCase(postId)
            .onSuccess { fetchPosts(state.items.size.coerceAtLeast(PAGE_SIZE)) }
            .onFailure { postSideEffect(CallvanListSideEffect.ShowSnackbar(it.toListErrorType())) }
    }

    fun updateFilterVisible(visible: Boolean) = blockingIntent {
        if (visible) {
            reduce { state.copy(isFilterVisible = true, pendingFilterState = state.filterState) }
        } else {
            reduce { state.copy(isFilterVisible = false) }
        }
    }

    fun onFilterItemClicked(item: CallvanFilterType) = blockingIntent {
        val pending = state.pendingFilterState
        reduce {
            state.copy(
                pendingFilterState = when (item) {
                    is ListType -> pending.copy(selectedListType = item)
                    is SortType -> pending.copy(selectedSortType = item)
                    is StatusesType -> pending.copy(selectedStatusesType = item)
                    is DeparturesFilterType -> pending.copy(
                        selectedDeparturesType = toggleDuplicateSelection(
                            pending.selectedDeparturesType,
                            item,
                            DeparturesFilterType.All
                        )
                    )
                    is ArrivalsFilterType -> pending.copy(
                        selectedArrivalsType = toggleDuplicateSelection(
                            pending.selectedArrivalsType,
                            item,
                            ArrivalsFilterType.All
                        )
                    )
                }
            )
        }
    }

    fun resetPendingFilter() = blockingIntent {
        reduce { state.copy(pendingFilterState = FilterBottomSheetState()) }
    }

    fun applyPendingFilter() = intent {
        val pending = state.pendingFilterState
        if (pending.selectedListType !is ListType.All && !state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        reduce { state.copy(filterState = pending, isFilterVisible = false) }
        fetchPosts()
    }

    private fun <T : CallvanFilterType> toggleDuplicateSelection(
        current: ImmutableList<T>,
        item: T,
        allItem: T
    ): ImmutableList<T> = when {
        item == allItem -> persistentListOf(allItem)
        allItem in current -> persistentListOf(item)
        item in current -> {
            if (current.size > MINIMUM_SELECTION_COUNT) {
                (current - item).toPersistentList()
            } else {
                current
            }
        }
        else -> (current + item).toPersistentList()
    }

    fun updatePendingConfirm(pending: Pair<CallvanConfirmType, Int>?) = blockingIntent {
        reduce { state.copy(pendingConfirm = pending) }
    }

    fun updatePendingCompletePostId(postId: Int?) = blockingIntent {
        reduce { state.copy(pendingCompletePostId = postId) }
    }

    fun updateLoginVisible(visible: Boolean) = blockingIntent {
        reduce { state.copy(isLoginVisible = visible) }
    }

    fun checkNotificationSuggest() = intent {
        if (!state.isLoggedIn) return@intent
        val shouldOnboard = onboardingManager.getShouldOnboard(OnboardingType.CALLVAN_NOTIFICATION)
        if (!shouldOnboard) return@intent
        onboardingManager.updateShouldOnboard(OnboardingType.CALLVAN_NOTIFICATION, false)
        getNotificationPermissionInfoUseCase().onSuccess { info ->
            val isCallvanEnabled = info.subscribes.any { it.type == SubscribesType.CALLVAN && it.isPermit }
            if (!isCallvanEnabled) {
                reduce { state.copy(showNotificationSuggest = true) }
            }
        }
    }

    fun enableCallvanNotification() = intent {
        updateNotificationSubscriptionUseCase(SubscribesType.CALLVAN)
            .onSuccess { reduce { state.copy(showNotificationSuggest = false) } }
            .onFailure {
                reduce { state.copy(showNotificationSuggest = false) }
                postSideEffect(CallvanListSideEffect.ShowSnackbar(CallvanListErrorType.NOTIFICATION_SUBSCRIPTION_FAILED))
            }
    }

    fun dismissNotificationSuggest() = blockingIntent {
        reduce { state.copy(showNotificationSuggest = false) }
    }

    fun fetchPosts(limit: Int = PAGE_SIZE) = intent {
        reduce { state.copy(isLoading = true) }
        getCallvanPostsUseCase(
            author = state.filterState.selectedListType.value,
            departures = state.filterState.selectedDeparturesType.mapNotNull { it.value }.ifEmpty { null },
            departureKeyword = null,
            arrivals = state.filterState.selectedArrivalsType.mapNotNull { it.value }.ifEmpty { null },
            arrivalKeyword = null,
            statuses = state.filterState.selectedStatusesType.value?.let { listOf(it) },
            title = state.searchValue.ifBlank { null },
            sort = state.filterState.selectedSortType.value,
            joined = state.filterState.selectedListType is ListType.Joined,
            page = 1,
            limit = limit
        ).onSuccess { result ->
            reduce {
                state.copy(
                    items = result.posts.map { it.toUiState() }.toPersistentList(),
                    currentPage = result.currentPage,
                    totalPage = result.totalPage,
                    hasMoreItems = result.currentPage < result.totalPage,
                    isLoading = false
                )
            }
        }.onFailure {
            reduce { state.copy(isLoading = false) }
        }
    }

    fun loadMorePosts() = intent {
        if (state.isLoadingMore || !state.hasMoreItems) return@intent
        reduce { state.copy(isLoadingMore = true) }
        val nextPage = state.currentPage + 1
        getCallvanPostsUseCase(
            author = state.filterState.selectedListType.value,
            departures = state.filterState.selectedDeparturesType.mapNotNull { it.value }.ifEmpty { null },
            departureKeyword = null,
            arrivals = state.filterState.selectedArrivalsType.mapNotNull { it.value }.ifEmpty { null },
            arrivalKeyword = null,
            statuses = state.filterState.selectedStatusesType.value?.let { listOf(it) },
            title = state.searchValue.ifBlank { null },
            sort = state.filterState.selectedSortType.value,
            joined = state.filterState.selectedListType is ListType.Joined,
            page = nextPage,
            limit = PAGE_SIZE
        ).onSuccess { result ->
            reduce {
                state.copy(
                    items = (state.items + result.posts.map { it.toUiState() }).distinctBy { it.id }.toPersistentList(),
                    currentPage = result.currentPage,
                    totalPage = result.totalPage,
                    hasMoreItems = result.currentPage < result.totalPage,
                    isLoadingMore = false
                )
            }
        }.onFailure {
            reduce { state.copy(isLoadingMore = false) }
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val SEARCH_DEBOUNCE_MS = 250L
        private const val MINIMUM_SELECTION_COUNT = 1
    }
}
