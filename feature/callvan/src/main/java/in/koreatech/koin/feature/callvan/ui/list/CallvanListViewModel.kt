package `in`.koreatech.koin.feature.callvan.ui.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.callvan.CloseCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.CompleteCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanPostsUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.callvan.JoinCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.LeaveCallvanPostUseCase
import `in`.koreatech.koin.domain.usecase.callvan.ReopenCallvanPostUseCase
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanItemState
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListUiState
import `in`.koreatech.koin.feature.callvan.ui.list.model.FilterBottomSheetState
import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
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
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel(), ContainerHost<CallvanListState, CallvanListSideEffect> {

    override val container = container<CallvanListState, CallvanListSideEffect>(
        CallvanListState()
    )

    init {
        fetchPosts()
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
        fetchPosts()
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
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        joinCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
    }

    fun cancelJoin(index: Int) = intent {
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        leaveCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
    }

    fun close(index: Int) = intent {
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        closeCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
    }

    fun reRecruit(index: Int) = intent {
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        reopenCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
    }

    fun complete(index: Int) = intent {
        val postId = state.items.getOrNull(index)?.id ?: return@intent
        completeCallvanPostUseCase(postId)
            .onSuccess { fetchPosts() }
    }

    fun showLoginDialog() = intent {
        reduce { state.copy(isLoginVisible = true) }
    }

    fun dismissLoginDialog() = intent {
        reduce { state.copy(isLoginVisible = false) }
    }

    private fun fetchPosts() = intent {
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
            limit = null
        ).onSuccess { result ->
            reduce {
                state.copy(
                    items = result.posts.map { it.toUiState() }.toPersistentList(),
                    isLoading = false
                )
            }
        }.onFailure {
            reduce { state.copy(isLoading = false) }
        }
    }

    private fun CallvanPostSearch.CallvanPost.toItemState(): CallvanItemState = when {
        isAuthor && status == "RECRUITING" -> CallvanItemState.OWNER_ACTIVE
        isAuthor && status == "CLOSED" -> CallvanItemState.OWNER_CLOSED
        isJoined -> CallvanItemState.JOINED
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