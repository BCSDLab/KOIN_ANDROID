package `in`.koreatech.koin.feature.store.scroll

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

@Composable
fun storeCollapsingToolbarConnection(
    listState: LazyListState,
    toolbarOffsetPx: MutableState<Float>,
    toolbarHeightPx: Float,
    minHeightPx: Float
): NestedScrollConnection = remember(listState, toolbarOffsetPx) {
    object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            val currentOffset = toolbarOffsetPx.value
            val newOffset = if (
                listState.firstVisibleItemIndex == 0 &&
                listState.firstVisibleItemScrollOffset == 0
            ) {
                (currentOffset + delta).coerceIn(-toolbarHeightPx + minHeightPx, 0f)
            } else {
                -toolbarHeightPx + minHeightPx
            }
            val consumed = newOffset - currentOffset
            toolbarOffsetPx.value = newOffset

            return if (consumed != 0f) Offset(0f, consumed) else Offset.Zero
        }
    }
}
