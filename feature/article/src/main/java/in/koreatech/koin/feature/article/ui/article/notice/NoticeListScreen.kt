package `in`.koreatech.koin.feature.article.ui.article.notice

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.component.ArticleKeywordGroup
import `in`.koreatech.koin.feature.article.component.LoadingDialog
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import `in`.koreatech.koin.feature.article.ui.article.notice.component.ArticleItem
import `in`.koreatech.koin.feature.article.ui.article.state.ArticlePaginationState
import `in`.koreatech.koin.feature.article.ui.lostandfound.list.component.LostAndFoundPagination
import kotlinx.coroutines.launch

@Composable
fun NoticeListScreen(
    board: ArticleBoardType,
    modifier: Modifier = Modifier,
    viewModel: ArticleListNoticeViewModel = hiltViewModel(),
    navigateToArticleDetail: (articleId: Int, boardId: Int) -> Unit = { _, _ -> },
    navigateToKeywordSetting: () -> Unit = {}
) {
    LaunchedEffect(board) {
        viewModel.setCurrentBoard(board)
    }

    val isLoading by viewModel.isLoading.observeAsState(false)
    val articlePaginationState by viewModel.articlePagination.collectAsState()
    val myKeywords by viewModel.myKeywords.collectAsState()
    val selectedKeyword by viewModel.selectedKeyword.collectAsState()

    if (isLoading) {
        LoadingDialog()
    }

    NoticeListScreen(
        articlePaginationState = articlePaginationState,
        myKeywords = myKeywords,
        selectedKeyword = selectedKeyword,
        modifier = modifier,
        onKeywordSelected = viewModel::selectKeyword,
        onPageChanged = viewModel::setCurrentPage,
        navigateToArticleDetail = navigateToArticleDetail,
        navigateToKeywordSetting = navigateToKeywordSetting
    )
}

@Composable
private fun NoticeListScreen(
    articlePaginationState: ArticlePaginationState,
    myKeywords: List<String>,
    selectedKeyword: String,
    modifier: Modifier = Modifier,
    onKeywordSelected: (String) -> Unit = {},
    onPageChanged: (Int) -> Unit = {},
    navigateToArticleDetail: (articleId: Int, boardId: Int) -> Unit = { _, _ -> },
    navigateToKeywordSetting: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect {
            EventLogger.logScrollEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.NOTICE_PAGE,
                context.getString(R.string.title_article)
            )
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        item {
            ArticleKeywordGroup(
                keyWords = myKeywords,
                selectedKeywordIndex = if (selectedKeyword.isEmpty()) 0 else myKeywords.indexOf(selectedKeyword) + 1,
                navigateToKeywordFragment = navigateToKeywordSetting,
                selectKeyword = onKeywordSelected
            )
        }

        items(articlePaginationState.articles) {
            ArticleItem(
                boardType = it.board,
                title = it.title,
                author = it.author,
                registeredAt = it.updatedAt,
                viewCount = it.viewCount
            ) {
                navigateToArticleDetail(it.id, it.board.id)
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            LostAndFoundPagination(
                modifier = Modifier.fillMaxWidth(),
                currentPage = articlePaginationState.currentPage,
                totalPage = articlePaginationState.totalPage,
                onPageChange = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                    onPageChanged(it)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
