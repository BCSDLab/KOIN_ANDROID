package `in`.koreatech.koin.feature.store.state

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity


class CustomCollapsingToolbarState(
    val toolbarMinHeight: Dp = 40.dp,
    val toolbarMaxHeight: Dp = 300.dp,
    val toolbarOffsetPx: MutableFloatState = mutableFloatStateOf(0f),
    val listState: LazyListState = LazyListState()
) {
    var toolbarHeightPx :Float = 0f
        private set
    var minHeightPx :Float = 0f
        private set

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun currentToolbarHeightDp(toolbarState: CustomCollapsingToolbarState): Dp {
        val density = LocalDensity.current
        return remember {
            derivedStateOf {
                with(density) {
                    (toolbarHeightPx + toolbarState.toolbarOffsetPx.floatValue).toDp()
                }
            }
        }.value
    }

    @Composable
    fun progress(toolbarState: CustomCollapsingToolbarState): Float {
        val density = LocalDensity.current
        toolbarHeightPx = with(density) { toolbarMaxHeight.toPx() }
        minHeightPx = with(density) { toolbarState.toolbarMinHeight.toPx() }
        return remember {
            derivedStateOf {
                val offset = toolbarState.toolbarOffsetPx.floatValue
                val range = toolbarHeightPx - minHeightPx
                ((-offset) / range).coerceIn(0f, 1f)
            }
        }.value
    }

    @Composable
    fun rememberCollapsingToolbarState(
        toolbarMinHeight: Dp = 40.dp,
        toolbarMaxHeight: Dp = 300.dp
    ): CustomCollapsingToolbarState {
        val listState = remember { LazyListState() }
        val toolbarOffsetPx = remember { mutableFloatStateOf(0f) }
        return CustomCollapsingToolbarState(
            toolbarMinHeight = toolbarMinHeight,
            toolbarMaxHeight = toolbarMaxHeight,
            toolbarOffsetPx = toolbarOffsetPx,
            listState = listState
        )
    }
}