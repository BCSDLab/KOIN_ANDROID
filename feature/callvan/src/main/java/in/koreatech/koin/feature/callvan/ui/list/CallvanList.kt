package `in`.koreatech.koin.feature.callvan.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.callvan.LOAD_MORE_THRESHOLD
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.SEARCH_DEBOUNCE_MS
import `in`.koreatech.koin.feature.callvan.enums.CallvanRouteState
import `in`.koreatech.koin.feature.callvan.model.CallvanListItemClickListener
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanNotificationIcon
import `in`.koreatech.koin.feature.callvan.ui.list.component.CallvanFAB
import `in`.koreatech.koin.feature.callvan.ui.list.component.CallvanFilterChip
import `in`.koreatech.koin.feature.callvan.ui.list.component.CallvanListItem
import `in`.koreatech.koin.feature.callvan.ui.list.component.CompleteBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.list.component.ConfirmBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.list.component.FilterBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.list.component.ItemSearchTextField
import `in`.koreatech.koin.feature.callvan.ui.list.component.LoginBottomSheet
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(FlowPreview::class)
@Composable
fun CallvanList(
    viewModel: CallvanListViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    navigateToCreate: () -> Unit = {},
    navigateToNotifications: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            CallvanListSideEffect.FetchData -> viewModel.fetchCallvanArticles()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { uiState.searchQuery }
            .debounce(SEARCH_DEBOUNCE_MS)
            .collect { viewModel.fetchCallvanArticles() }
    }

    if (uiState.showFilterBottomSheet) {
        FilterBottomSheet(
            onDismissRequest = { viewModel.setShowFilterBottomSheet(false) },
            selectedSortType = uiState.sortType,
            selectedStatusesType = uiState.statusesType,
            selectedDeparturesType = uiState.departuresFilterType,
            selectedArrivalsType = uiState.arrivalsFilterType,
            onApply = { sort, status, departures, arrivals ->
                viewModel.setSearchFilter(sort, status, departures, arrivals)
            }
        )
    }

    if (uiState.showLoginBottomSheet) {
        LoginBottomSheet(
            onLogin = {
                viewModel.setShowLoginBottomSheet(false)
                navigateToLogin()
            },
            onDismiss = { viewModel.setShowLoginBottomSheet(false) }
        )
    }

    if (uiState.showConfirmBottomSheet && uiState.confirmType != null) {
        ConfirmBottomSheet(
            confirmType = uiState.confirmType!!,
            onConfirm = { viewModel.onConfirmAction() },
            onDismiss = { viewModel.dismissConfirmBottomSheet() }
        )
    }

    if (uiState.showCompleteBottomSheet) {
        CompleteBottomSheet(
            onConfirm = { viewModel.onCompleteAction() },
            onDismiss = { viewModel.dismissCompleteBottomSheet() }
        )
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.callvan_title),
                onNavigationIconClick = onTopbarBackClick,
                actions = {
                    IconButton(onClick = navigateToNotifications) {
                        CallvanNotificationIcon()
                    }
                }
            )
        },
        floatingActionButton = {
            CallvanFAB(
                onClick = {
                    if (uiState.isLoggedIn) {
                        navigateToCreate()
                    } else {
                        viewModel.setShowLoginBottomSheet(true)
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
                .imePadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemSearchTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp),
                    value = uiState.searchQuery,
                    onValueChange = viewModel::setSearchQuery
                )
                CallvanFilterChip(
                    onClick = { viewModel.setShowFilterBottomSheet(true) }
                )
            }

            when {
                uiState.isFirstPageLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = KoinTheme.colors.primary500,
                            strokeWidth = 2.dp
                        )
                    }
                }

                uiState.articles.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.callvan_empty_articles),
                            style = KoinTheme.typography.bold20
                        )
                    }
                }

                else -> {
                    val listState = rememberLazyListState()

                    val shouldLoadMore by remember {
                        derivedStateOf {
                            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            val total = listState.layoutInfo.totalItemsCount
                            uiState.hasMore && !uiState.isLoadingMore && lastVisible >= total - LOAD_MORE_THRESHOLD
                        }
                    }

                    LaunchedEffect(shouldLoadMore) {
                        if (shouldLoadMore) viewModel.loadMoreArticles()
                    }

                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.articles,
                            key = { it.id }
                        ) { item ->
                            CallvanListItem(
                                uiState = item,
                                clickListener = object : CallvanListItemClickListener {
                                    override fun onJoin() {
                                        if (uiState.isLoggedIn) {
                                            viewModel.showConfirmBottomSheet(item.id, CallvanRouteState.DEFAULT)
                                        } else {
                                            viewModel.setShowLoginBottomSheet(true)
                                        }
                                    }
                                    override fun onCancelJoin() {
                                        viewModel.showConfirmBottomSheet(item.id, CallvanRouteState.JOINED)
                                    }
                                    override fun onClose() {
                                        viewModel.showConfirmBottomSheet(item.id, CallvanRouteState.OWNER_ACTIVE)
                                    }
                                    override fun onReRecruit() {
                                        viewModel.showConfirmBottomSheet(item.id, CallvanRouteState.OWNER_CLOSED)
                                    }
                                    override fun onComplete() {
                                        viewModel.showCompleteBottomSheet(item.id)
                                    }
                                    override fun onCall() { /* TODO: 전화 인텐트 */ }
                                    override fun onChat() { /* TODO: 채팅 화면 네비게이션 */ }
                                }
                            )
                        }

                        if (uiState.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = KoinTheme.colors.primary500,
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
