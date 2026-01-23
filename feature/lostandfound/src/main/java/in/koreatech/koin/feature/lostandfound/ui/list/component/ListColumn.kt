package `in`.koreatech.koin.feature.lostandfound.ui.list.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.model.LostAndFoundItemState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

private const val LOAD_MORE_THRESHOLD = 3

@Composable
fun ListColumn(
    searchedArticles: ImmutableList<LostAndFoundItemState>,
    isLoadingMore: Boolean,
    hasMoreArticles: Boolean,
    onLoadMore: () -> Unit,
    onArticleClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, hasMoreArticles, isLoadingMore, searchedArticles.size) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)
            val canScroll = layoutInfo.totalItemsCount > layoutInfo.visibleItemsInfo.size
            if (!canScroll && totalItemsCount > 0) {
                true
            } else {
                lastVisibleItemIndex >= totalItemsCount - LOAD_MORE_THRESHOLD
            }
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                if (hasMoreArticles && !isLoadingMore) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = searchedArticles,
            key = { article -> article.id }
        ) { article ->
            ListItem(
                articleId = article.id,
                lostOrFound = article.lostOrFound,
                lostItemCategory = article.category,
                foundPlace = article.foundPlace,
                content = article.content,
                author = article.author,
                isReported = article.isReported,
                isFound = article.isFound,
                foundDate = article.foundDate,
                registeredAt = article.registeredAt,
                onArticleClick = onArticleClick
            )
            HorizontalDivider(color = KoinTheme.colors.neutral100)
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
                        color = KoinTheme.colors.primary500,
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}
