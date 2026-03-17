package `in`.koreatech.koin.feature.callvan.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.callvan.CloseCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.CompleteCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanPostsUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.callvan.JoinCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.LeaveCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.ReopenCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanConfirmType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanItemState
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListUiState
import `in`.koreatech.koin.feature.callvan.ui.list.model.FilterBottomSheetState
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CallvanListViewModel @Inject constructor(
    private val getCallvanPostsUseCase: GetCallvanPostsUseCase,
    private val joinCallvanPostUseCase: JoinCallvanPostUseCase,
    private val leaveCallvanPostUseCase: LeaveCallvanPostUseCase,
    private val closeCallvanPostUseCase: CloseCallvanPostUseCase,
    private val reopenCallvanPostUseCase: ReopenCallvanPostUseCase,
    private val completeCallvanPostUseCase: CompleteCallvanPostUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<CallvanListState, CallvanListSideEffect> {

    override val container = container<CallvanListState, CallvanListSideEffect>(
        CallvanListState()
    )

    init {
        fetchPosts()
        initUserInfo()
    }

    private fun initUserInfo() = viewModelScope.launch {
        getUserStatusUseCase().collectLatest { user ->
            intent {
                reduce {
                    state.copy(isLoggedIn = user !is User.Anonymous)
                }
            }
        }
    }

    internal fun fetchHasNewNotification() = intent {
        getNotificationsUseCase()
            .onSuccess { notifications ->
                reduce {
                    state.copy(hasNewNotification = notifications.any { !it.isRead })
                }
            }
    }

    fun updateSearch(query: String) = intent {
        reduce { state.copy(searchValue = query) }
    }

    fun applyFilter(
        sortType: CallvanFilterType.SortType,
        statusesType: CallvanFilterType.StatusesType,
        departuresType: ImmutableList<CallvanFilterType.DeparturesFilterType>,
        arrivalsType: ImmutableList<CallvanFilterType.ArrivalsFilterType>
    ) = intent {
        reduce {
            state.copy(
                filterState = FilterBottomSheetState(
                    selectedSortType = sortType,
                    selectedStatusesType = statusesType,
                    selectedDeparturesType = departuresType,
                    selectedArrivalsType = arrivalsType
                )
            )
        }
        fetchPosts()
    }

    fun join(index: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        joinCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
            .onFailure { Log.e("MYLOG", "join ${it}") }
    }

    fun cancelJoin(index: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        leaveCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
            .onFailure { Log.e("MYLOG", "joinCancle ${it}") }
    }

    fun close(index: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        closeCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
            .onFailure { Log.e("MYLOG", "close ${it}") }
    }

    fun reRecruit(index: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        reopenCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
            .onFailure { Log.e("MYLOG", "reRecruit ${it}") }
    }

    fun complete(index: Int) = intent {
        if (!state.isLoggedIn) {
            reduce { state.copy(isLoginVisible = true) }
            return@intent
        }
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        completeCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
    }

    fun updateFilterVisible(visible: Boolean) = intent {
        reduce { state.copy(isFilterVisible = visible) }
    }

    fun updatePendingConfirm(pending: Pair<CallvanConfirmType, Int>?) = intent {
        reduce { state.copy(pendingConfirm = pending) }
    }

    fun updatePendingCompleteIndex(index: Int?) = intent {
        reduce { state.copy(pendingCompleteIndex = index) }
    }

    fun updateLoginVisible(visible: Boolean) = intent {
        reduce { state.copy(isLoginVisible = visible) }
    }

    fun fetchPosts() = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        getCallvanPostsUseCase(
            author = null,
            departures = state.filterState.selectedDeparturesType.mapNotNull { it.value }.ifEmpty { null },
            departureKeyword = null,
            arrivals = state.filterState.selectedArrivalsType.mapNotNull { it.value }.ifEmpty { null },
            arrivalKeyword = null,
            statuses = state.filterState.selectedStatusesType.value?.let { listOf(it) },
            title = state.searchValue.ifBlank { null },
            sort = state.filterState.selectedSortType.value,
            page = 1,
            limit = PAGE_SIZE
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
            Log.e("MYLOG","not load ${it}")
            reduce { state.copy(isLoading = false) }
        }
    }

    fun loadMorePosts() = intent {
        if (state.isLoadingMore || !state.hasMoreItems) return@intent
        reduce { state.copy(isLoadingMore = true) }
        val nextPage = state.currentPage + 1
        getCallvanPostsUseCase(
            author = null,
            departures = state.filterState.selectedDeparturesType.mapNotNull { it.value }.ifEmpty { null },
            departureKeyword = null,
            arrivals = state.filterState.selectedArrivalsType.mapNotNull { it.value }.ifEmpty { null },
            arrivalKeyword = null,
            statuses = state.filterState.selectedStatusesType.value?.let { listOf(it) },
            title = state.searchValue.ifBlank { null },
            sort = state.filterState.selectedSortType.value,
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
    }

    private fun CallvanPostSearch.CallvanPost.toItemState(): CallvanItemState = when {
        isAuthor && status == "RECRUITING" -> CallvanItemState.OWNER_ACTIVE
        isAuthor && status == "CLOSED" -> CallvanItemState.OWNER_CLOSED
        !isAuthor && isJoined -> CallvanItemState.JOINED
        status == "RECRUITING" -> CallvanItemState.DEFAULT
        else -> CallvanItemState.CLOSED
    }

    private fun CallvanPostSearch.CallvanPost.toUiState(): CallvanListUiState = CallvanListUiState(
        id = id,
        departure = departure,
        destination = arrival,
        date = departureDate,
        time = departureTime,
        currentCount = currentParticipants,
        maxCount = maxParticipants,
        itemState = toItemState()
    )
}