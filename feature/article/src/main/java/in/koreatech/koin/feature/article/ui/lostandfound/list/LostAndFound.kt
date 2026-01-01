package `in`.koreatech.koin.feature.article.ui.lostandfound.list

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.component.LoadingDialog
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
import `in`.koreatech.koin.feature.article.ui.article.state.LostAndFoundPaginationState
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.component.LostAndFoundDialog
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.component.LostAndFoundDropdownGroup
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.component.LostAndFoundFAB
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.component.LostAndFoundItem
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.component.LostAndFoundKeywordGroup
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.component.LostAndFoundPagination
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.component.lostAndFoundDialogStyle
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun LostAndFoundList(
    modifier: Modifier = Modifier,
    viewModel: LostAndFoundViewModel = hiltViewModel(),
    myKeywords: List<String>,
    selectedKeyword: String,
    selectedType: LostOrFoundType?,
    lostAndFoundPaginationState: LostAndFoundPaginationState,
    currentPage: Int,
    navigateToWriteFoundItem: (lostOrFoundType: String) -> Unit = {},
    navigateToLostAndFoundDetail: (articleId: Int) -> Unit = {},
    navigateToKeywordFragment: () -> Unit = {},
    navigateToLoginActivity: () -> Unit = {},
    onKeywordChange: (String) -> Unit = {},
    onLostOrFoundChange: (LostOrFoundType?) -> Unit = {},
    onPageChange: (Int) -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    val isLoading = uiState.isLoading
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val firstItemPosition by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    val isScrolledToTheEnd by remember { derivedStateOf { lazyListState.isScrolledToTheEnd() } }
    val fabBottomPadding: Dp by animateDpAsState(
        if (isScrolledToTheEnd && firstItemPosition != 0) {
            64.dp
        } else {
            0.dp
        }
    )

    val context = LocalContext.current

    KoinTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = KoinTheme.colors.neutral0,
            floatingActionButton = {
                val fabWrite = stringResource(R.string.fab_write)
                val fabFoundText = stringResource(R.string.fab_found)
                val fabLostText = stringResource(R.string.fab_lost)
                LostAndFoundFAB(
                    modifier = Modifier
                        .padding(bottom = fabBottomPadding)
                        .consumeWindowInsets(WindowInsets.navigationBars),
                    isDialogExpanded = uiState.isFabDialogExpanded,
                    dialogExpandButtonText = fabWrite,
                    dialogExpandButtonPainter = painterResource(id = R.drawable.ic_fab_write),
                    firstButtonText = fabFoundText,
                    firstButtonPainter = painterResource(id = R.drawable.ic_found),
                    secondButtonText = fabLostText,
                    secondButtonPainter = painterResource(id = R.drawable.ic_lost),
                    onFirstButtonClick = {
                        if (uiState.isAnonymous) {
                            viewModel.setShowLoginRequestDialog(true)
                        } else {
                            EventLogger.logCampusClickEvent(
                                AnalyticsConstant.Label.LostAndFound.FOUND_WRITE,
                                fabFoundText
                            )
                            navigateToWriteFoundItem(
                                LostOrFoundType.FOUND.name
                            )
                        }
                        viewModel.setFabDialogExpanded(false)
                    },
                    onSecondButtonClick = {
                        if (uiState.isAnonymous) {
                            viewModel.setShowLoginRequestDialog(true)
                        } else {
                            EventLogger.logCampusClickEvent(
                                AnalyticsConstant.Label.LostAndFound.LOST_WRITE,
                                fabLostText
                            )
                            navigateToWriteFoundItem(
                                LostOrFoundType.LOST.name
                            )
                        }
                        viewModel.setFabDialogExpanded(false)
                    },
                    onDialogExpandedChange = {
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.LostAndFound.ITEM_WRITE,
                            fabWrite
                        )
                        viewModel.setFabDialogExpanded(it)
                    }
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { contentPadding ->
            Column(
                modifier = modifier
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
            ) {
                LazyColumn(
                    modifier = Modifier,
                    state = lazyListState
                ) {
                    item {
                        LostAndFoundKeywordGroup(
                            keyWords = myKeywords,
                            selectedKeywordIndex = when (selectedKeyword) {
                                "" -> 0
                                else -> myKeywords.indexOf(selectedKeyword) + 1
                            },
                            navigateToKeywordFragment = navigateToKeywordFragment,
                            selectKeyword = onKeywordChange
                        )
                        LostAndFoundDropdownGroup(
                            selectedType = selectedType,
                            isDropdownExpanded = uiState.isDropdownExpanded,
                            onDropdownExpandChange = {
                                viewModel.setDropdownExpanded(it)
                            },
                            onItemSelected = {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.LostAndFound.ITEM_POST_TYPE,
                                    when (it) {
                                        0 -> "물품 전체"
                                        1 -> "습득물"
                                        2 -> "분실물"
                                        else -> ""
                                    }
                                )
                                onLostOrFoundChange(
                                    when (it) {
                                        0 -> null
                                        1 -> LostOrFoundType.FOUND
                                        2 -> LostOrFoundType.LOST
                                        else -> null
                                    }
                                )
                            }
                        )
                    }
                    if (lostAndFoundPaginationState.articleLostAndFoundHeader.isEmpty()) {
                        item {
                            Text(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                text = stringResource(R.string.empty_articles)
                            )
                        }
                    } else {
                        items(lostAndFoundPaginationState.articleLostAndFoundHeader) {
                            LostAndFoundItem(
                                lostOrFound = it.lostOrFound,
                                lostItemCategory = it.category,
                                foundPlace = it.foundPlace,
                                content = it.content,
                                author = it.author,
                                isReported = it.isReported,
                                foundDate = it.foundDate,
                                registeredAt = it.registeredAt
                            ) {
                                if (it.isReported) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.reported_article_click_toast),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    navigateToLostAndFoundDetail(it.id)
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                        item {
                            LostAndFoundPagination(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                currentPage = currentPage,
                                totalPage = lostAndFoundPaginationState.totalPage,
                                onPageChange = onPageChange
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        item {
                            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                        }
                    }
                }

                if (isLoading) {
                    LoadingDialog()
                }

                if (uiState.showLoginRequestDialog) {
                    LostAndFoundDialog(
                        title = stringResource(R.string.request_login_dialog_title),
                        description = stringResource(R.string.request_login_dialog_description),
                        lostAndFoundDialogStyle = lostAndFoundDialogStyle().copy(
                            titleStyle = KoinTheme.typography.medium18.copy(textAlign = TextAlign.Center),
                            descriptionStyle = KoinTheme.typography.regular14.copy(textAlign = TextAlign.Center)
                        ),
                        onPositive = {
                            EventLogger.logCampusClickEvent(
                                AnalyticsConstant.Label.LOGIN_PROMPT,
                                "게시글 작성 팝업"
                            )
                            navigateToLoginActivity()
                            viewModel.setShowLoginRequestDialog(false)
                        },
                        onNegative = {
                            viewModel.setShowLoginRequestDialog(false)
                        }
                    )
                }
            }
        }
    }
}

fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
