package `in`.koreatech.koin.feature.store.state

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class CustomCollapsingToolbarState(
    val toolbarMinHeight: Dp = 40.dp,
    val toolbarMaxHeight: Dp = 300.dp,
    val toolbarOffsetPx: MutableFloatState = mutableFloatStateOf(0f),
    val listState: LazyListState = LazyListState()
) {
    var toolbarHeightPx: Float = 0f
    var minHeightPx: Float = 0f
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

@Composable
fun CustomCollapsingToolbarState.currentToolbarHeightDp(): Dp {
    val density = LocalDensity.current
    return remember(toolbarOffsetPx.floatValue) {
        derivedStateOf {
            with(density) {
                (toolbarMaxHeight.toPx() + toolbarOffsetPx.floatValue).toDp()
            }
        }
    }.value
}

@Composable
fun CustomCollapsingToolbarState.progress(): Float {
    val density = LocalDensity.current
    toolbarHeightPx = with(density) { toolbarMaxHeight.toPx() }
    minHeightPx = with(density) { toolbarMinHeight.toPx() }
    return remember {
        derivedStateOf {
            val offset = toolbarOffsetPx.floatValue
            val range = toolbarHeightPx - minHeightPx
            ((-offset) / range).coerceIn(0f, 1f)
        }
    }.value
}

fun CustomCollapsingToolbarState.collapseToolbar(
    state: CustomCollapsingToolbarState
) {
    state.toolbarOffsetPx.value = -toolbarHeightPx + minHeightPx
}
