package `in`.koreatech.koin.feature.lostandfound.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.AuthorFilterType.MY
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.ItemSearchTextField
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.ListColumn
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.LostAndFoundChip
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.LostAndFoundFAB
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.LostAndFoundFABBottomSheet
import `in`.koreatech.koin.feature.lostandfound.ui.list.component.LostAndFoundFilterBottomSheet
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun LostAndFoundList(
    viewModel: LostAndFoundListViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    navigateArticleDetail: (Int) -> Unit = {},
    navigateToWrite: (String) -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    if (uiState.showFilterBottomSheet) {
        LostAndFoundFilterBottomSheet(
            onDismissRequest = {
                viewModel.setShowFilterBottomSheet(false)
            },
            selectedAuthorType = uiState.authorFilterType,
            selectedLostOrFoundType = uiState.lostOrFoundFilterType,
            selectedCategoryType = uiState.categoryFilterType,
            selectedFoundType = uiState.foundFilterType,
            onApply = { first, second, third, fourth ->
                if (!uiState.isLoggedIn && first == MY) {
                    viewModel.setShowLoginDialog(true)
                } else {
                    viewModel.setSearchFilter(
                        authorFilterType = first,
                        lostOrFoundFilterType = second,
                        categoryFilterType = third,
                        foundFilterType = fourth
                    )
                    viewModel.fetchLostAndFoundItem()
                }
            }
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

    if (uiState.showLoginDialog) {
        ChoiceDialog(
            title = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in),
            description = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in_description),
            positiveButtonText = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in_positive),
            negativeButtonText = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in_negative),
            onPositive = {
                navigateToLogin()
                viewModel.setShowLoginDialog(false)
            },
            onNegative = {
                viewModel.setShowLoginDialog(false)
            },
            titleStyle = KoinTheme.typography.medium18.copy(color = KoinTheme.colors.neutral600, textAlign = TextAlign.Center),
            descriptionStyle = KoinTheme.typography.regular14.copy(color = Color(0xFF8E8E8E))
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
                onClick = {
                    viewModel.setShowWriteBottomSheet(true)
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
                    .padding(horizontal = 24.dp),
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
