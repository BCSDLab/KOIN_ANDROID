package `in`.koreatech.koin.feature.callvan.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.callvan.CloseCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.CompleteCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanPostsUseCase
import `in`.koreatech.koin.domain.usecase.callvan.JoinCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.LeaveCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.ReopenCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.callvan.PAGE_SIZE
import `in`.koreatech.koin.feature.callvan.STATUS_CLOSED
import `in`.koreatech.koin.feature.callvan.STATUS_RECRUITING
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.ArrivalsFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.DeparturesFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.StatusesType
import `in`.koreatech.koin.feature.callvan.enums.CallvanRouteState
import `in`.koreatech.koin.feature.callvan.enums.ConfirmType
import `in`.koreatech.koin.feature.callvan.model.CallvanListUiState
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class CallvanListViewModel @Inject constructor(
    private val getCallvanPostsUseCase: GetCallvanPostsUseCase,
    private val joinCallvanPostUseCase: JoinCallvanPostUseCase,
    private val leaveCallvanPostUseCase: LeaveCallvanPostUseCase,
    private val closeCallvanPostUseCase: CloseCallvanPostUseCase,
    private val completeCallvanPostUseCase: CompleteCallvanPostUseCase,
    private val reopenCallvanPostUseCase: ReopenCallvanPostUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<CallvanListState, CallvanListSideEffect> {

    override val container = container<CallvanListState, CallvanListSideEffect>(
        initialState = CallvanListState()
    )

    init {
        initUserInfo()
    }

    private fun initUserInfo() = viewModelScope.launch {
        getUserStatusUseCase().collectLatest {
            intent {
                reduce {
                    state.copy(
                        isLoggedIn = it is User.Student || it is User.General
                    )
                }
            }
        }
    }

    fun fetchCallvanArticles() = intent {
        if (state.isLoading) return@intent

        reduce {
            state.copy(
                isLoading = true,
                isFirstPageLoading = true,
                isLoadingMore = true
            )
        }

        getCallvanPostsUseCase(
            author = null,
            departures = state.departuresFilterType.toDepartureApiValues(),
            departureKeyword = null,
            arrivals = state.arrivalsFilterType.toArrivalApiValues(),
            arrivalKeyword = null,
            statuses = state.statusesType.toApiValue(),
            title = state.searchQuery.ifBlank { null },
            sort = state.sortType.value,
            page = 1,
            limit = PAGE_SIZE
        ).onSuccess { result ->
            reduce {
                state.copy(
                    articles = result.posts.map { it.toUiState() }.toPersistentList(),
                    currentPage = result.currentPage,
                    totalPage = result.totalPage,
                    hasMore = result.currentPage < result.totalPage,
                    isLoading = false,
                    isFirstPageLoading = false,
                    isLoadingMore = false
                )
            }
        }.onFailure {
            Timber.e(it)
            reduce {
                state.copy(
                    articles = persistentListOf(),
                    hasMore = false,
                    isLoading = false,
                    isFirstPageLoading = false,
                    isLoadingMore = false
                )
            }
        }
    }

    fun loadMoreArticles() = intent {
        if (state.isLoadingMore || !state.hasMore) return@intent

        reduce { state.copy(isLoadingMore = true) }

        val nextPage = state.currentPage + 1

        getCallvanPostsUseCase(
            author = null,
            departures = state.departuresFilterType.toDepartureApiValues(),
            departureKeyword = null,
            arrivals = state.arrivalsFilterType.toArrivalApiValues(),
            arrivalKeyword = null,
            statuses = state.statusesType.toApiValue(),
            title = state.searchQuery.ifBlank { null },
            sort = state.sortType.value,
            page = nextPage,
            limit = PAGE_SIZE
        ).onSuccess { result ->
            reduce {
                state.copy(
                    articles = (state.articles + result.posts.map { it.toUiState() })
                        .distinctBy { it.id }
                        .toPersistentList(),
                    currentPage = result.currentPage,
                    totalPage = result.totalPage,
                    hasMore = result.currentPage < result.totalPage,
                    isLoadingMore = false
                )
            }
        }.onFailure {
            Timber.e(it)
            reduce { state.copy(isLoadingMore = false) }
        }
    }

    fun setSearchFilter(
        sortType: SortType,
        statusesType: StatusesType,
        departuresFilterType: ImmutableList<DeparturesFilterType>,
        arrivalsFilterType: ImmutableList<ArrivalsFilterType>
    ) = intent {
        reduce {
            state.copy(
                sortType = sortType,
                statusesType = statusesType,
                departuresFilterType = departuresFilterType,
                arrivalsFilterType = arrivalsFilterType
            )
        }
        postSideEffect(CallvanListSideEffect.FetchData)
    }

    fun setSearchQuery(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
    }

    fun setShowFilterBottomSheet(show: Boolean) = intent {
        reduce { state.copy(showFilterBottomSheet = show) }
    }

    fun setShowLoginBottomSheet(show: Boolean) = intent {
        reduce { state.copy(showLoginBottomSheet = show) }
    }

    fun showConfirmBottomSheet(itemId: Int, routeState: CallvanRouteState) = intent {
        val confirmType = ConfirmType.from(routeState)
        if (confirmType != null) {
            reduce {
                state.copy(
                    showConfirmBottomSheet = true,
                    confirmType = confirmType,
                    selectedItemId = itemId
                )
            }
        }
    }

    fun dismissConfirmBottomSheet() = intent {
        reduce {
            state.copy(
                showConfirmBottomSheet = false,
                confirmType = null,
                selectedItemId = null
            )
        }
    }

    fun showCompleteBottomSheet(itemId: Int) = intent {
        reduce {
            state.copy(
                showCompleteBottomSheet = true,
                selectedItemId = itemId
            )
        }
    }

    fun dismissCompleteBottomSheet() = intent {
        reduce {
            state.copy(
                showCompleteBottomSheet = false,
                selectedItemId = null
            )
        }
    }

    fun onConfirmAction() = intent {
        val postId = state.selectedItemId ?: return@intent
        val confirmType = state.confirmType ?: return@intent

        reduce {
            state.copy(
                showConfirmBottomSheet = false,
                confirmType = null,
                selectedItemId = null
            )
        }

        val result = when (confirmType) {
            ConfirmType.JOIN -> joinCallvanPostUseCase(postId)
            ConfirmType.CANCEL_JOIN -> leaveCallvanPostUseCase(postId)
            ConfirmType.CLOSE -> closeCallvanPostUseCase(postId)
            ConfirmType.REOPEN -> reopenCallvanPostUseCase(postId)
        }

        result.onSuccess {
            postSideEffect(CallvanListSideEffect.FetchData)
        }.onFailure {
            Timber.e(it)
        }
    }

    fun onCompleteAction() = intent {
        val postId = state.selectedItemId ?: return@intent

        reduce {
            state.copy(
                showCompleteBottomSheet = false,
                selectedItemId = null
            )
        }

        completeCallvanPostUseCase(postId)
            .onSuccess {
                postSideEffect(CallvanListSideEffect.FetchData)
            }.onFailure {
                Timber.e(it)
            }
    }

    private fun CallvanPostSearch.CallvanPost.toUiState(): CallvanListUiState {
        return CallvanListUiState(
            id = id,
            departure = departure,
            destination = arrival,
            date = departureDate,
            time = departureTime,
            currentCount = currentParticipants,
            maxCount = maxParticipants,
            routeState = toRouteState()
        )
    }

    private fun CallvanPostSearch.CallvanPost.toRouteState(): CallvanRouteState {
        return when {
            isAuthor && status == STATUS_RECRUITING -> CallvanRouteState.OWNER_ACTIVE
            isAuthor && status == STATUS_CLOSED -> CallvanRouteState.OWNER_CLOSED
            isJoined -> CallvanRouteState.JOINED
            status == STATUS_CLOSED -> CallvanRouteState.CLOSED
            else -> CallvanRouteState.DEFAULT
        }
    }

    private fun ImmutableList<DeparturesFilterType>.toDepartureApiValues(): List<String>? {
        return if (size == 1 && first() == DeparturesFilterType.All) {
            null
        } else {
            map { it.value }
        }
    }

    private fun ImmutableList<ArrivalsFilterType>.toArrivalApiValues(): List<String>? {
        return if (size == 1 && first() == ArrivalsFilterType.All) {
            null
        } else {
            map { it.value }
        }
    }

    private fun StatusesType.toApiValue(): List<String>? {
        return if (this == StatusesType.All) {
            null
        } else {
            listOf(value)
        }
    }
}
