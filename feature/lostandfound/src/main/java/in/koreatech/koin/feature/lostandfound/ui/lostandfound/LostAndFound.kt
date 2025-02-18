package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.LoadingDialog
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundDialog
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundDropdownGroup
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundFAB
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundItem
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundKeywordGroup
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.LostAndFoundPagination
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.component.lostAndFoundDialogStyle
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LostAndFoundList(
    viewModel: LostAndFoundViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navigateToWriteFoundItem: (lostOrFoundType: String) -> Unit = {},
    navigateToLostAndFoundDetail: (articleId: Int) -> Unit = {},
    navigateToKeywordFragment: () -> Unit = {},
    navigateToLoginActivity: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(sideEffect, viewModel)
    }
    val isLoading = uiState.isLoading

    LaunchedEffect(uiState.selectedKeyword) {
        viewModel.fetchLostAndFoundList()
    }

    val lazyListState = rememberLazyListState()
    val firstItemPosition by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    val fabBottomPadding: Dp by animateDpAsState(
        if (lazyListState.isScrolledToTheEnd() && firstItemPosition != 0) {
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
                    modifier = Modifier.padding(bottom = fabBottomPadding),
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
                                AnalyticsConstant.Label.LOST_AND_FOUND.FOUND_WRITE,
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
                                AnalyticsConstant.Label.LOST_AND_FOUND.LOST_WRITE,
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
                            AnalyticsConstant.Label.LOST_AND_FOUND.ITEM_WRITE,
                            fabWrite
                        )
                        viewModel.setFabDialogExpanded(it)
                    }
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { contentPadding ->
            val myKeywords = uiState.myKeywords
            Column(
                modifier = modifier
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
            ) {
                LazyColumn(
                    modifier = modifier,
                    state = lazyListState
                ) {
                    item {
                        LostAndFoundKeywordGroup(
                            keyWords = myKeywords,
                            selectedKeywordIndex = when (uiState.selectedKeyword) {
                                "" -> 0
                                else -> myKeywords.indexOf(uiState.selectedKeyword) + 1
                            },
                            navigateToKeywordFragment = navigateToKeywordFragment
                        ) {
                            viewModel.selectKeyword(it)
                        }
                        LostAndFoundDropdownGroup(
                            selectedType = uiState.selectedType,
                            isDropdownExpanded = uiState.isDropdownExpanded,
                            onDropdownExpandChange = {
                                viewModel.setDropdownExpanded(it)
                            },
                            onItemSelected = {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.LOST_AND_FOUND.ITEM_POST_TYPE,
                                    when (it) {
                                        0 -> "물품 전체"
                                        1 -> "습득물"
                                        2 -> "분실물"
                                        else -> ""
                                    }
                                )
                                viewModel.setSelectedType(
                                    when (it) {
                                        0 -> null
                                        1 -> LostOrFoundType.FOUND
                                        2 -> LostOrFoundType.LOST
                                        else -> null
                                    }
                                )
                                viewModel.changePage(1)
                                viewModel.fetchLostAndFoundList()
                            }
                        )
                    }
                    if (uiState.lostAndFoundList.isEmpty()) {
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
                        items(uiState.lostAndFoundList) {
                            LostAndFoundItem(
                                lostOrFound = it.lostOrFound,
                                lostItemCategory = it.category,
                                foundPlace = it.foundPlace,
                                content = it.content,
                                author = it.author,
                                isReported = it.isReported,
                                foundDate = it.foundDate,
                                registeredAt = it.registeredAt,
                            ) {
                                if (it.isReported) {
                                    Toast.makeText(context, context.getString(R.string.reported_article_click_toast), Toast.LENGTH_SHORT).show()
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
                                currentPage = uiState.currentPage,
                                totalPage = uiState.totalPage
                            ) {
                                viewModel.changePage(it)
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }

                LaunchedEffect(uiState.currentPage) {
                    lazyListState.scrollToItem(0)
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
                            descriptionStyle = KoinTheme.typography.regular14.copy(textAlign = TextAlign.Center),
                        ),
                        onPositive = {
                            navigateToLoginActivity()
                            viewModel.setShowLoginRequestDialog(false)
                        },
                        onNegative = {
                            viewModel.setShowLoginRequestDialog(false)
                        },
                    )
                }
            }
        }

    }
}

fun handleSideEffect(
    sideEffect: LostAndFoundSideEffect,
    viewModel: LostAndFoundViewModel
) {
    when (sideEffect) {
        is LostAndFoundSideEffect.PageChanged -> {
            viewModel.fetchLostAndFoundList()
        }
    }
}

fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1