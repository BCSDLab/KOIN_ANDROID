package `in`.koreatech.koin.feature.dining.ui.diningdetail.scroll

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

fun diningScrollConnection(
    currentScrollState: State<ScrollState>,
    toolbarOffsetPx: MutableState<Float>,
    maxToolbarHeightPx: Float,
    minToolbarHeightPx: Float
) = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        var delta = available.y
        if (available.y > 0) {
            val scrollState = currentScrollState.value
            if (scrollState.value <= delta) {
                delta -= scrollState.value
            } else {
                return Offset.Zero
            }
        }
        val newOffset = toolbarOffsetPx.value + delta
        val beforeToolbarOffsetPx = toolbarOffsetPx.value
        toolbarOffsetPx.value = newOffset.coerceIn(-(maxToolbarHeightPx - minToolbarHeightPx), 0f)
        return Offset(0f, toolbarOffsetPx.value - beforeToolbarOffsetPx)
   }
}