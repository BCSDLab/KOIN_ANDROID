package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

@Suppress("LongParameterList")
@Composable
fun CallvanScrollPicker(
    items: ImmutableList<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 32.dp,
    textAlign: TextAlign = TextAlign.Center,
    onIndexChange: (Int) -> Unit = {}
) {
    if (items.isEmpty()) {
        Box(modifier = modifier.height(itemHeight * 3))
    } else {
        CallvanScrollPickerContent(
            items = items,
            selectedIndex = selectedIndex,
            modifier = modifier,
            itemHeight = itemHeight,
            textAlign = textAlign,
            onIndexChange = onIndexChange
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun CallvanScrollPickerContent(
    items: ImmutableList<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 32.dp,
    textAlign: TextAlign = TextAlign.Center,
    onIndexChange: (Int) -> Unit = {}
) {
    val density = LocalDensity.current
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedIndex.coerceIn(0, items.lastIndex)
    )
    val snappingLayout = rememberSnapFlingBehavior(listState)
    val latestOnIndexChange by rememberUpdatedState(onIndexChange)
    var isProgrammaticScroll by remember { mutableStateOf(false) }

    val currentSelectedIndex by remember(density, items.size, itemHeight) {
        derivedStateOf {
            val itemHeightPx = with(density) { itemHeight.toPx() }
            val totalOffset = listState.firstVisibleItemIndex * itemHeightPx +
                listState.firstVisibleItemScrollOffset
            (totalOffset / itemHeightPx).roundToInt()
                .coerceIn(0, items.lastIndex)
        }
    }

    LaunchedEffect(listState) {
        var hasScrollStarted = false
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling) {
                    if (!isProgrammaticScroll) hasScrollStarted = true
                } else if (hasScrollStarted) {
                    hasScrollStarted = false
                    latestOnIndexChange(currentSelectedIndex)
                }
            }
    }

    LaunchedEffect(selectedIndex, items.size) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .first()

        val target = selectedIndex.coerceIn(0, items.lastIndex)
        if (listState.firstVisibleItemIndex != target || listState.firstVisibleItemScrollOffset != 0) {
            isProgrammaticScroll = true
            try {
                listState.scrollToItem(target)
            } finally {
                isProgrammaticScroll = false
            }
        }
    }

    LazyColumn(
        state = listState,
        flingBehavior = snappingLayout,
        contentPadding = PaddingValues(vertical = itemHeight),
        modifier = modifier.height(itemHeight * 3)
    ) {
        itemsIndexed(items, key = { index, _ -> index }) { index, item ->
            val isSelected by remember(index) { derivedStateOf { index == currentSelectedIndex } }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    style = RebrandKoinTheme.typography.medium16,
                    color = if (isSelected) RebrandKoinTheme.colors.neutral800 else RebrandKoinTheme.colors.neutral500,
                    textAlign = textAlign
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanScrollPickerPreview() {
    RebrandKoinTheme {
        CallvanScrollPicker(
            items = persistentListOf("1월", "2월", "3월", "4월", "5월", "6월"),
            selectedIndex = 2,
            onIndexChange = {}
        )
    }
}
