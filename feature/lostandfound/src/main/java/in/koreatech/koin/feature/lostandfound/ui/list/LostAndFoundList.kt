package `in`.koreatech.koin.feature.lostandfound.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.ItemSearchTextField
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.KeywordNotificationRow
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.ListColumn
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.LoginDialog
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.LostAndFoundChip
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.LostAndFoundFAB
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.LostAndFoundFABBottomSheet
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.LostAndFoundFilterBottomSheet
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

@Composable
fun LostAndFoundList(
    cancelRefresh: Boolean,
    viewModel: LostAndFoundListViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    navigateArticleDetail: (Int) -> Unit = {},
    navigateToWrite: (String) -> Unit = {},
    navigateToKeywordSetting: (List<String>) -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(
            sideEffect = sideEffect,
            fetchData = viewModel::fetchLostAndFoundItem,
            updateSignInDialog = {
                viewModel.setShowFilterLoginDialog(it)
            }
        )
    }

    LaunchedEffect(cancelRefresh) {
        var cancelRefresh = cancelRefresh
        snapshotFlow { uiState.searchQuery }
            .debounce(SEARCH_DEBOUNCE_MS)
            .distinctUntilChanged()
            .collect {
                if (cancelRefresh) {
                    cancelRefresh = false
                } else {
                    viewModel.intent { postSideEffect(LostAndFoundListSideEffect.FetchData) }
                }
            }
    }

    if (uiState.showFilterBottomSheet) {
        LostAndFoundFilterBottomSheet(
            onDismissRequest = {
                viewModel.setShowFilterBottomSheet(false)
            },
            selectedAuthorType = uiState.authorFilterType,
            selectedLostOrFoundType = uiState.lostOrFoundFilterType,
            selectedCategoryType = uiState.categoryFilterType,
            selectedFoundType = uiState.foundFilterType,
            onApply = viewModel::setSearchFilter
        )
    }

    if (uiState.showWriteBottomSheet) {
        LostAndFoundFABBottomSheet(
            onDismissRequest = {
                viewModel.setShowWriteBottomSheet(false)
            },
            onFindOwnerClick = {
                navigateToWrite(LostOrFoundType.FOUND.name)
            },
            onLostItemClick = {
                navigateToWrite(LostOrFoundType.LOST.name)
            }
        )
    }

    if (uiState.showFilterLoginDialog) {
        LoginDialog(
            title = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in),
            description = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in_description),
            onPositive = {
                navigateToLogin()
                viewModel.intent { postSideEffect(LostAndFoundListSideEffect.UpdateSignInDialog(false)) }
            },
            onNegative = {
                viewModel.intent { postSideEffect(LostAndFoundListSideEffect.UpdateSignInDialog(false)) }
            }
        )
    }

    if (uiState.showWriteLoginDialog) {
        LoginDialog(
            title = stringResource(id = R.string.request_login_dialog_title),
            description = stringResource(id = R.string.request_login_dialog_description),
            onPositive = {
                EventLogger.logCampusClickEvent(
                    AnalyticsConstant.Label.LostAndFound.LOST_ITEM_WRITE_LOGIN_REQUEST,
                    "로그인하기"
                )
                navigateToLogin()
                viewModel.setShowWriteLoginDialog(false)
            },
            onNegative = {
                EventLogger.logCampusClickEvent(
                    AnalyticsConstant.Label.LostAndFound.LOST_ITEM_WRITE_LOGIN_REQUEST,
                    "닫기"
                )
                viewModel.setShowWriteLoginDialog(false)
            }
        )
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.lost_and_found),
                onNavigationIconClick = onTopbarBackClick
            )
        },
        floatingActionButton = {
            LostAndFoundFAB(
                modifier = Modifier.offset(y = 10.dp),
                onClick = {
                    if (uiState.isLoggedIn) {
                        viewModel.setShowWriteBottomSheet(true)
                    } else {
                        viewModel.setShowWriteLoginDialog(true)
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
                    .padding(
                        horizontal = 24.dp,
                        vertical = 4.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemSearchTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp),
                    value = uiState.searchQuery,
                    onValueChange = viewModel::setSearchQuery
                )
                LostAndFoundChip(
                    onClick = {
                        viewModel.setShowFilterBottomSheet(true)
                    }
                )
            }
            KeywordNotificationRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                keywords = uiState.keywords,
                selectedKeywordIndex = uiState.selectedKeywordIndex,
                onKeywordSelect = viewModel::selectKeyword,
                onSettingClick = { navigateToKeywordSetting(uiState.keywords) }
            )
            if (!uiState.isFirstPageLoading) {
                if (uiState.searchedArticles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.empty_articles),
                            style = KoinTheme.typography.bold20
                        )
                    }
                } else {
                    ListColumn(
                        searchedArticles = uiState.searchedArticles.toPersistentList(),
                        isLoadingMore = uiState.isLoadingMoreArticles,
                        hasMoreArticles = uiState.hasMoreArticles,
                        onLoadMore = { viewModel.loadMoreLostAndFoundItem() },
                        onArticleClick = navigateArticleDetail
                    )
                }
            } else {
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
        }
    }
}

private fun handleSideEffect(
    sideEffect: LostAndFoundListSideEffect,
    fetchData: () -> Unit,
    updateSignInDialog: (visible: Boolean) -> Unit
) {
    when (sideEffect) {
        LostAndFoundListSideEffect.FetchData -> {
            fetchData()
        }

        is LostAndFoundListSideEffect.UpdateSignInDialog -> {
            updateSignInDialog(sideEffect.visible)
        }
    }
}

private const val SEARCH_DEBOUNCE_MS = 250L
