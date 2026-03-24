package `in`.koreatech.koin.feature.callvan.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.snackbar.CustomSnackBarHost
import `in`.koreatech.koin.core.designsystem.component.snackbar.showSnackBarWithDismiss
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanConfirmBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanNotificationIcon
import `in`.koreatech.koin.feature.callvan.ui.list.component.CallvanFAB
import `in`.koreatech.koin.feature.callvan.ui.list.component.CallvanFilterChip
import `in`.koreatech.koin.feature.callvan.ui.list.component.CallvanListItem
import `in`.koreatech.koin.feature.callvan.ui.list.component.FilterBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.list.component.ItemSearchTextField
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanConfirmType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanItemState
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListErrorType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListItemActions
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListUiState
import `in`.koreatech.koin.feature.callvan.ui.list.model.FilterBottomSheetState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CallvanListScreen(
    viewModel: CallvanListViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onWriteClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onChatClick: (postId: Int) -> Unit = {},
    onCallClick: (postId: Int) -> Unit = {},
    onDetailClick: (postId: Int) -> Unit = {}
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CallvanListSideEffect.ShowSnackbar -> {
                val message = context.getString(
                    when (sideEffect.errorType) {
                        CallvanListErrorType.POST_NOT_RECRUITING -> R.string.callvan_error_post_not_recruiting
                        CallvanListErrorType.POST_FULL -> R.string.callvan_error_post_full
                        CallvanListErrorType.ALREADY_JOINED -> R.string.callvan_error_already_joined
                        CallvanListErrorType.NOT_FOUND_ARTICLE -> R.string.callvan_error_not_found_article
                        CallvanListErrorType.FORBIDDEN_AUTHOR -> R.string.callvan_error_forbidden_author
                        CallvanListErrorType.FORBIDDEN_PARTICIPANT -> R.string.callvan_error_forbidden_participant
                        CallvanListErrorType.POST_AUTHOR_CANNOT_LEAVE -> R.string.callvan_error_post_author_cannot_leave
                        CallvanListErrorType.REOPEN_FAILED_FULL -> R.string.callvan_error_reopen_failed_full
                        CallvanListErrorType.REOPEN_FAILED_TIME -> R.string.callvan_error_reopen_failed_time
                        CallvanListErrorType.UNKNOWN -> R.string.callvan_error_unknown
                    }
                )
                snackbarHostState.showSnackBarWithDismiss(message)
            }
        }
    }

    LaunchedEffect(state.isLoggedIn) {
        viewModel.fetchHasNewNotification()
    }

    CallvanListScreenImpl(
        searchValue = state.searchValue,
        items = state.items,
        filterState = state.filterState,
        hasNewNotification = state.hasNewNotification,
        isLoginVisible = state.isLoginVisible,
        isFilterVisible = state.isFilterVisible,
        isLoadingMore = state.isLoadingMore,
        hasMoreItems = state.hasMoreItems,
        pendingConfirm = state.pendingConfirm,
        pendingCompletePostId = state.pendingCompletePostId,
        onSearchValueChange = viewModel::updateSearch,
        onFilterApply = viewModel::applyFilter,
        onFilterVisibleChange = viewModel::updateFilterVisible,
        onPendingConfirmChange = viewModel::updatePendingConfirm,
        onPendingCompletePostIdChange = viewModel::updatePendingCompletePostId,
        onLoadMore = viewModel::loadMorePosts,
        onTopbarBackClick = onTopbarBackClick,
        onNotificationClick = onNotificationClick,
        onWriteClick = onWriteClick,
        onLoginClick = onLoginClick,
        onLoginVisibleChange = viewModel::updateLoginVisible,
        onJoin = viewModel::join,
        onCancelJoin = viewModel::cancelJoin,
        onClose = viewModel::close,
        onReRecruit = viewModel::reRecruit,
        onComplete = viewModel::complete,
        onCall = onCallClick,
        onChat = onChatClick,
        onDetailClick = onDetailClick,
        snackbarHostState = snackbarHostState
    )
}

@Suppress("LongParameterList", "CyclomaticComplexMethod")
@Composable
fun CallvanListScreenImpl(
    searchValue: String,
    items: ImmutableList<CallvanListUiState>,
    filterState: FilterBottomSheetState = FilterBottomSheetState(),
    hasNewNotification: Boolean = false,
    isLoginVisible: Boolean = false,
    isFilterVisible: Boolean = false,
    isLoadingMore: Boolean = false,
    hasMoreItems: Boolean = true,
    pendingConfirm: Pair<CallvanConfirmType, Int>? = null,
    pendingCompletePostId: Int? = null,
    onSearchValueChange: (String) -> Unit = {},
    onFilterVisibleChange: (Boolean) -> Unit = {},
    onPendingConfirmChange: (Pair<CallvanConfirmType, Int>?) -> Unit = {},
    onPendingCompletePostIdChange: (Int?) -> Unit = {},
    onLoadMore: () -> Unit = {},
    onFilterApply: (
        CallvanFilterType.ListType,
        CallvanFilterType.SortType,
        CallvanFilterType.StatusesType,
        ImmutableList<CallvanFilterType.DeparturesFilterType>,
        ImmutableList<CallvanFilterType.ArrivalsFilterType>
    ) -> Unit = { _, _, _, _, _ -> },
    onTopbarBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onWriteClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLoginVisibleChange: (Boolean) -> Unit = {},
    onJoin: (Int) -> Unit = {},
    onCancelJoin: (Int) -> Unit = {},
    onClose: (Int) -> Unit = {},
    onReRecruit: (Int) -> Unit = {},
    onComplete: (Int) -> Unit = {},
    onCall: (Int) -> Unit = {},
    onChat: (Int) -> Unit = {},
    onDetailClick: (Int) -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, hasMoreItems, isLoadingMore, items.size) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val canScroll = totalItemsCount > layoutInfo.visibleItemsInfo.size
            if (!canScroll && totalItemsCount > 0) {
                true
            } else {
                lastVisibleItemIndex >= totalItemsCount - LOAD_MORE_THRESHOLD
            }
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                if (hasMoreItems && !isLoadingMore) onLoadMore()
            }
    }

    if (isFilterVisible) {
        FilterBottomSheet(
            onDismissRequest = { onFilterVisibleChange(false) },
            initialListType = filterState.selectedListType,
            initialSortType = filterState.selectedSortType,
            initialStatusesType = filterState.selectedStatusesType,
            initialArrivalsType = filterState.selectedArrivalsType,
            initialDeparturesType = filterState.selectedDeparturesType,
            onApply = { listType, sortType, statusesType, departuresFilterTypes, arrivalsFilterTypes ->
                EventLogger.logCampusClickEvent(
                    AnalyticsConstant.Label.Callvan.CALLVAN_FILTER_APPLY,
                    ""
                )
                onFilterApply(listType, sortType, statusesType, departuresFilterTypes, arrivalsFilterTypes)
            }
        )
    }

    pendingConfirm?.let { (confirmType, postId) ->
        val title = when (confirmType) {
            CallvanConfirmType.JOIN -> stringResource(R.string.callvan_confirm_join_title)
            CallvanConfirmType.CANCEL_JOIN -> stringResource(R.string.callvan_confirm_cancel_title)
            CallvanConfirmType.CLOSE -> stringResource(R.string.callvan_confirm_close_title)
            CallvanConfirmType.REOPEN -> stringResource(R.string.callvan_confirm_reopen_title)
        }
        CallvanConfirmBottomSheet(
            title = title,
            description = "",
            confirmText = stringResource(R.string.callvan_confirm_positive),
            cancelText = stringResource(R.string.callvan_confirm_negative),
            onConfirm = {
                when (confirmType) {
                    CallvanConfirmType.JOIN -> {
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.Callvan.CALLVAN_JOIN,
                            ""
                        )
                        onJoin(postId)
                    }
                    CallvanConfirmType.CANCEL_JOIN -> {
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.Callvan.CALLVAN_JOIN_CANCEL,
                            ""
                        )
                        onCancelJoin(postId)
                    }
                    CallvanConfirmType.CLOSE -> onClose(postId)
                    CallvanConfirmType.REOPEN -> onReRecruit(postId)
                }
                onPendingConfirmChange(null)
            },
            onDismiss = { onPendingConfirmChange(null) }
        )
    }

    pendingCompletePostId?.let { postId ->
        CallvanConfirmBottomSheet(
            title = stringResource(R.string.callvan_complete_title),
            description = stringResource(R.string.callvan_complete_description),
            confirmText = stringResource(R.string.callvan_confirm_positive),
            cancelText = stringResource(R.string.callvan_confirm_negative),
            onConfirm = {
                onComplete(postId)
                onPendingCompletePostIdChange(null)
            },
            onDismiss = { onPendingCompletePostIdChange(null) }
        )
    }

    if (isLoginVisible) {
        CallvanConfirmBottomSheet(
            title = stringResource(R.string.callvan_login_title),
            description = "",
            confirmText = stringResource(R.string.callvan_login_login),
            cancelText = stringResource(R.string.callvan_login_close),
            onConfirm = {
                onLoginClick()
                onLoginVisibleChange(false)
            },
            onDismiss = { onLoginVisibleChange(false) }
        )
    }

    Box {
        Scaffold(
            topBar = {
                KoinTopAppBar(
                    title = stringResource(R.string.filter_list_top_bar),
                    onNavigationIconClick = {
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.Callvan.CALLVAN_BACK,
                            ""
                        )
                        onTopbarBackClick()
                    },
                    actions = {
                        IconButton(onClick = onNotificationClick) {
                            CallvanNotificationIcon(hasNewNotification = hasNewNotification)
                        }
                    }
                )
            },
            floatingActionButton = {
                CallvanFAB(
                    modifier = Modifier.padding(bottom = 24.dp),
                    onClick = {
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.Callvan.CALLVAN_CREATE,
                            ""
                        )
                        onWriteClick()
                    }
                )
            },
            containerColor = RebrandKoinTheme.colors.neutral0
        ) { contentPadding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                stickyHeader {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RebrandKoinTheme.colors.neutral0)
                            .padding(vertical = 8.dp)
                    ) {
                        ItemSearchTextField(
                            value = searchValue,
                            onValueChange = onSearchValueChange,
                            modifier = Modifier.weight(1f).noRippleClickable {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.Callvan.CALLVAN_SEARCH,
                                    ""
                                )
                            }
                        )
                        CallvanFilterChip(onClick = { onFilterVisibleChange(true) })
                    }
                }

                items(items, key = { it.id }) { uiState ->
                    val actions = remember(uiState.id) {
                        CallvanListItemActions(
                            onJoin = { onPendingConfirmChange(Pair(CallvanConfirmType.JOIN, uiState.id)) },
                            onCancelJoin = { onPendingConfirmChange(Pair(CallvanConfirmType.CANCEL_JOIN, uiState.id)) },
                            onClose = { onPendingConfirmChange(Pair(CallvanConfirmType.CLOSE, uiState.id)) },
                            onReRecruit = { onPendingConfirmChange(Pair(CallvanConfirmType.REOPEN, uiState.id)) },
                            onComplete = { onPendingCompletePostIdChange(uiState.id) },
                            onCall = {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.Callvan.CALLVAN_CALL,
                                    ""
                                )
                                onCall(uiState.id)
                            },
                            onChat = {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.Callvan.CALLVAN_CHAT,
                                    ""
                                )
                                onChat(uiState.id)
                            }
                        )
                    }
                    CallvanListItem(
                        uiState = uiState,
                        onItemClick = { onDetailClick(uiState.id) },
                        actions = actions
                    )
                }

                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = RebrandKoinTheme.colors.primary500,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }
        CustomSnackBarHost(
            hotState = snackbarHostState,
            background = RebrandKoinTheme.colors.primary700.copy(alpha = 0.8f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListScreenPreview() {
    RebrandKoinTheme {
        CallvanListScreenImpl(
            searchValue = "",
            items = persistentListOf(
                CallvanListUiState(1, "테니스장", "천안 시외터미널", "2025-02-05", "14:00", 1, 8, CallvanItemState.DEFAULT),
                CallvanListUiState(2, "정문", "천안 시외터미널", "2025-02-05", "14:00", 1, 8, CallvanItemState.JOINED),
                CallvanListUiState(3, "테니스장", "천안역", "2025-02-05", "14:00", 1, 8, CallvanItemState.CLOSED),
                CallvanListUiState(4, "담헌 앞", "천안아산역", "2025-02-05", "14:00", 1, 8, CallvanItemState.OWNER_ACTIVE),
                CallvanListUiState(5, "천안 시외터미널", "학교", "2025-02-05", "14:00", 1, 8, CallvanItemState.OWNER_CLOSED)
            )
        )
    }
}

private const val LOAD_MORE_THRESHOLD = 3
