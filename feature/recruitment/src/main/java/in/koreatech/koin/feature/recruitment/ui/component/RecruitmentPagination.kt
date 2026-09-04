package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

private const val DEFAULT_LOAD_MORE_THRESHOLD = 3

@Composable
fun rememberRecruitmentPaginationListState(
    hasMore: Boolean,
    isLoadingMore: Boolean,
    itemCount: Int,
    onLoadMore: () -> Unit,
    loadMoreThreshold: Int = DEFAULT_LOAD_MORE_THRESHOLD
): LazyListState {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, hasMore, isLoadingMore, itemCount) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= layoutInfo.totalItemsCount - loadMoreThreshold
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { if (hasMore && !isLoadingMore) onLoadMore() }
    }

    return listState
}
