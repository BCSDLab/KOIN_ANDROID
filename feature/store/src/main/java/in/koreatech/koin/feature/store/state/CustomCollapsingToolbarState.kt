package `in`.koreatech.koin.feature.store.state

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class CustomCollapsingToolbarState(
    val toolbarMinHeight: Dp = 60.dp,
    val toolbarMaxHeight: Dp = 300.dp,
    val toolbarOffsetPx: MutableFloatState = mutableFloatStateOf(0f),
    val listState: LazyListState = LazyListState(),
    val toolbarHeightPx: Float = 0f,
    val minHeightPx: Float = 0f
) {
    @Stable
    @Composable
    fun progress(): State<Float> {
        return remember {
            derivedStateOf {
                val offset = toolbarOffsetPx.floatValue
                val range = toolbarHeightPx - minHeightPx
                ((-offset) / range).coerceIn(0f, 1f)
            }
        }
    }

    @Composable
    fun currentToolbarHeightDp(): State<Dp> {
        val density = LocalDensity.current
        return remember(toolbarOffsetPx) {
            derivedStateOf {
                with(density) {
                    (toolbarHeightPx + toolbarOffsetPx.floatValue).toDp()
                }
            }
        }
    }
}

@Composable
fun rememberCollapsingToolbarState(
    toolbarMinHeight: Dp = 60.dp,
    toolbarMaxHeight: Dp = 300.dp
): CustomCollapsingToolbarState {
    val listState = remember { LazyListState() }
    val toolbarOffsetPx = remember { mutableFloatStateOf(0f) }
    return CustomCollapsingToolbarState(
        toolbarMinHeight = toolbarMinHeight,
        toolbarMaxHeight = toolbarMaxHeight,
        toolbarOffsetPx = toolbarOffsetPx,
        listState = listState,
        toolbarHeightPx = with(LocalDensity.current) {
            toolbarMaxHeight.toPx()
        },
        minHeightPx = with(LocalDensity.current) {
            toolbarMinHeight.toPx()
        }
    )
}

fun CustomCollapsingToolbarState.collapseToolbar() {
    toolbarOffsetPx.value = -toolbarHeightPx + minHeightPx
}
