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
import androidx.compose.runtime.remember
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

@Suppress("LongParameterList")
@Composable
fun CallvanScrollPicker(
    items: ImmutableList<String>,
    selectedIndex: Int,
    onIndexChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    itemHeight: Dp = 32.dp,
    textAlign: TextAlign = TextAlign.Center
) {
    if (items.isEmpty()) {
        Box(modifier = modifier.height(itemHeight * 3))
        return
    }

    val density = LocalDensity.current
    val clampedIndex = remember(selectedIndex, items.size) {
        selectedIndex.coerceIn(0, items.lastIndex)
    }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = clampedIndex)
    val snappingLayout = rememberSnapFlingBehavior(listState)

    val currentSelectedIndex by remember(density, items.size) {
        derivedStateOf {
            val itemHeightPx = with(density) { itemHeight.toPx() }
            val totalOffset = listState.firstVisibleItemIndex * itemHeightPx +
                listState.firstVisibleItemScrollOffset
            (totalOffset / itemHeightPx).roundToInt()
                .coerceIn(0, items.lastIndex)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it }
            .collect { onIndexChange(currentSelectedIndex) }
    }

    LaunchedEffect(selectedIndex) {
        if (!listState.isScrollInProgress) {
            val target = selectedIndex.coerceIn(0, items.lastIndex)
            listState.scrollToItem(target)
        }
    }

    LazyColumn(
        state = listState,
        flingBehavior = snappingLayout,
        contentPadding = PaddingValues(vertical = itemHeight),
        modifier = modifier.height(itemHeight * 3)
    ) {
        itemsIndexed(items) { index, item ->
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
