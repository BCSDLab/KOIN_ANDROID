package `in`.koreatech.koin.feature.article.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
import `in`.koreatech.koin.feature.article.model.LostAndFoundItemState
import `in`.koreatech.koin.feature.article.ui.lostandfound.detail.component.LostAndFoundStatusChip
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

private const val LOAD_MORE_THRESHOLD = 3

@Composable
fun RecentArticleList(
    recentArticles: List<LostAndFoundItemState>,
    isLoadingMore: Boolean,
    hasMoreArticles: Boolean,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
    onArticleClick: (LostAndFoundItemState) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, hasMoreArticles, isLoadingMore, recentArticles.size) {
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

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 24.dp),
            style = KoinTheme.typography.bold16,
            text = stringResource(R.string.recent_article_title)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.height(400.dp)
        ) {
            items(
                items = recentArticles,
                key = { article -> article.id }
            ) { article ->
                RecentArticleItem(
                    article = article,
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
}

@Composable
private fun RecentArticleItem(
    article: LostAndFoundItemState,
    modifier: Modifier = Modifier,
    onArticleClick: (LostAndFoundItemState) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onArticleClick(article) }
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (article.lostOrFound == LostOrFoundType.LOST) "분실물" else "습득물",
            style = KoinTheme.typography.bold12.copy(
                fontWeight = FontWeight.SemiBold,
                color = KoinTheme.colors.primary600
            )
        )
        Spacer(modifier = Modifier.width(8.dp))

        LostItemTypeChip(category = article.category)
        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = article.content.ifEmpty { article.foundPlace },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = KoinTheme.typography.bold14.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        LostAndFoundStatusChip(isFound = article.isFound)
    }
}
