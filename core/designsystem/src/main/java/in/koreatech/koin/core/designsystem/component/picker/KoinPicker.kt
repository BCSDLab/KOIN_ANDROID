package `in`.koreatech.koin.core.designsystem.component.picker

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import kotlinx.coroutines.flow.map

/**
 * 아이템 피커
 * @param items 아이템 리스트
 * @param pickerState PickerState
 * @param visibleItemsCount 보여질 아이템 수
 * @param modifier Modifier
 * @param infiniteScroll 무한 스크롤 여부
 * @param brushVerticalGradient vertical Brush 그라디언트 설정
 * @param startIndex 시작 인덱스
 * @param contentPadding 아이템 내부 Padding
 * @param selectedTextStyle 선택 아이템 텍스트 스타일
 * @param unselectedTextStyle 선택되지 않은 아이템 텍스트 스타일
 * @param selectedItemColor 선택 아이템 색상
 * @param unselectedItemColor 선택되지 않은 아이템 색상
 */
@Composable
fun KoinPicker(
    items: List<String>,
    pickerState: PickerState,
    visibleItemsCount: Int,
    modifier: Modifier = Modifier,
    infiniteScroll: Boolean = true,
    brushVerticalGradient: Brush = verticalGradient(),
    startIndex: Int = 0,
    contentPadding: PaddingValues = PaddingValues(vertical = 2.dp),
    selectedTextStyle: TextStyle = KoinTheme.typography.medium16,
    unselectedTextStyle: TextStyle = KoinTheme.typography.medium16,
    selectedItemColor: Color = KoinTheme.colors.primary500,
    unselectedItemColor: Color = KoinTheme.colors.neutral500,
) {

    val newItems = if (infiniteScroll) items
    else List(visibleItemsCount / 2) { "" } + items + List(visibleItemsCount / 2) { "" }

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = if (infiniteScroll) Int.MAX_VALUE else newItems.size
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        (listScrollMiddle - listScrollMiddle % newItems.size - visibleItemsMiddle + startIndex).coerceAtLeast(startIndex)

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    var selectedItemIndex by remember { mutableIntStateOf(startIndex) }

    var itemHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val fadingEdgeGradient = remember { brushVerticalGradient }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        modifier = modifier
            .height(itemHeight * visibleItemsCount)
            .fadingEdge(fadingEdgeGradient),
    ) {
        items(listScrollCount) { index ->
            Text(
                text = newItems.getItem(index),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = if (selectedItemIndex == index) selectedTextStyle else unselectedTextStyle,
                color = if (selectedItemIndex == index) selectedItemColor else unselectedItemColor,
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged {
                        itemHeight = with(density) {
                            it.height.toDp()
                        }
                    }
                    .padding(contentPadding)
            )
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.map { index ->
            index + visibleItemsMiddle
        }.collect { middle ->
            selectedItemIndex = middle
            pickerState.selectedItem = newItems.getItem(middle)
            pickerState.selectedItemIndex = middle % newItems.size - if (infiniteScroll.not()) visibleItemsMiddle else 0
        }
    }
}

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState internal constructor() {
    var selectedItem by mutableStateOf("")
    var selectedItemIndex by mutableIntStateOf(0)
}

private fun Modifier.fadingEdge(brush: Brush) = if (brush == verticalGradient()) {
    this
} else {
    this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
                drawContent()
                drawRect(brush = brush, blendMode = BlendMode.DstIn)
        }
}


private fun List<String>.getItem(index: Int) = this[index % this.size]