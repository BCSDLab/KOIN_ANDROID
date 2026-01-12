package `in`.koreatech.koin.core.nestedscroll

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberKoinNestedScrollConnection(
    state: KoinNestedScrollHeaderState
): NestedScrollConnection {
    val scope = rememberCoroutineScope()

    return remember(state, scope) {
        KoinNestedScrollConnection(state = state, scope = scope)
    }
}

open class KoinNestedScrollConnection(
    private val state: KoinNestedScrollHeaderState,
    private val scope: CoroutineScope
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        val currentOffset = state.headerOffsetPx
        val headerExpandedHeightPx = state.headerExpandedHeightPx
        val headerCollapsedHeightPx = state.headerCollapsedHeightPx
        val collapsedOffset = -headerExpandedHeightPx + headerCollapsedHeightPx

        val isScrollingUp = delta > 0

        val newOffset = if (isScrollingUp) {
            currentOffset
        } else {
            (currentOffset + delta).coerceIn(collapsedOffset, 0f)
        }

        val consumed = newOffset - currentOffset

        if (consumed != 0f) {
            scope.launch {
                state.snapOffset(newOffset)
            }
        }

        return if (consumed != 0f) Offset(0f, consumed) else Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = available.y
        val currentOffset = state.headerOffsetPx
        val headerExpandedHeightPx = state.headerExpandedHeightPx
        val headerCollapsedHeightPx = state.headerCollapsedHeightPx
        val collapsedOffset = -headerExpandedHeightPx + headerCollapsedHeightPx

        val isScrollingUp = delta > 0

        if (isScrollingUp && currentOffset < 0f) {
            val newOffset = (currentOffset + delta).coerceIn(collapsedOffset, 0f)
            val consumedByHeader = newOffset - currentOffset

            if (consumedByHeader != 0f) {
                scope.launch {
                    state.snapOffset(newOffset)
                }
                return Offset(0f, consumedByHeader)
            }
        }

        return Offset.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        val collapsedOffset = -state.headerExpandedHeightPx + state.headerCollapsedHeightPx
        val expandedOffset = 0f
        val halfwayOffset = collapsedOffset / 2f

        val isHeaderPartiallyOpen = state.headerOffsetPx > collapsedOffset && state.headerOffsetPx < expandedOffset
        val isExpandedMoreThanHalf = state.headerOffsetPx > halfwayOffset

        if (isHeaderPartiallyOpen) {
            if (isExpandedMoreThanHalf) {
                state.expandHeader()
            } else {
                state.collapseHeader()
            }
        }

        return super.onPostFling(consumed, available)
    }
}
