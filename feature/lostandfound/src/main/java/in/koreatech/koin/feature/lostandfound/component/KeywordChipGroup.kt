package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.chip.ChipOverflowStrategy.Flow
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipColors
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun KeywordChipGroup(
    chipTextList: List<String>,
    vararg selectedChipIndexes: Int,
    modifier: Modifier = Modifier,
    onSelect: (index: Int) -> Unit
) {
    LostAndFoundTextChipScrollGroup(
        titles = chipTextList,
        selectedChipIndexes = selectedChipIndexes,
        onChipSelected = { onSelect(it) },
        showClickRipple = false,
        shape = RoundedCornerShape(50),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        chipColors = keywordChipColors(),
        modifier = modifier
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LostAndFoundTextChipFlowGroup(
    modifier: Modifier = Modifier,
    titles: List<String>,
    shape: Shape,
    onChipSelected: (index: Int) -> Unit,
    vararg selectedChipIndexes: Int,
    showClickRipple: Boolean,
    contentPadding: PaddingValues,
    horizontalArrangement: Arrangement.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    chipColors: TextChipColors
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        titles.forEachIndexed { index, it ->
            LostAndFoundTextChip(
                title = it,
                isSelected = selectedChipIndexes.contains(index),
                shape = shape,
                chipColors = chipColors,
                contentPadding = contentPadding,
                showClickRipple = showClickRipple,
                onSelect = { onChipSelected(index) }
            )
        }
    }
}

/**
 * 텍스트 칩 그룹
 * @see `in`.koreatech.koin.core.designsystem.component.chip.TextChipGroup
 * @param modifier Modifier
 * @param chipOverflowStrategy Chip이 화면을 넘었을 때 전략 : [Flow], [Scroll]
 * @param titles 칩 텍스트 리스트
 * @param shape 칩 모양
 * @param selectedChipIndexes 선택된 칩 인덱스 리스트
 * @param onChipSelected 칩 선택 리스너
 * @param showClickRipple 칩 클릭시 리플 효과 표시 여부
 * @param contentPadding 칩 내부 padding
 * @param horizontalArrangement 칩 가로 정렬
 * @param chipColors 칩 색상
 */
@Composable
fun LostAndFoundTextChipScrollGroup(
    titles: List<String>,
    shape: Shape,
    onChipSelected: (index: Int) -> Unit,
    vararg selectedChipIndexes: Int,
    showClickRipple: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
    horizontalArrangement: Arrangement.Horizontal,
    chipColors: TextChipColors = TextChipDefaults.chipColors()
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.horizontalGradient(
                        0f to Color.White,
                        0.1f to Color.Transparent,
                        0.9f to Color.Transparent,
                        1f to Color.White
                    )
                )
            },
        horizontalArrangement = horizontalArrangement
    ) {
        titles.forEachIndexed { index, it ->
            LostAndFoundTextChip(
                title = it,
                isSelected = selectedChipIndexes.contains(index),
                shape = shape,
                chipColors = chipColors,
                contentPadding = contentPadding,
                showClickRipple = showClickRipple,
                onSelect = { onChipSelected(index) }
            )
        }
    }
}


/**
 * 텍스트 칩
 * Design System의 TextChip에서 font padding을 제거한 버전
 * @see `in`.koreatech.koin.core.designsystem.component.chip.TextChip
 * @param title 텍스트
 * @param isSelected 선택 여부
 * @param shape 칩 모양
 * @param showClickRipple 클릭시 리플 효과 표시 여부
 * @param onSelect 클릭시 실행할 함수
 * @param contentPadding 칩 내부 padding
 * @param chipColors 칩 색상
 */
@Composable
fun LostAndFoundTextChip(
    title: String,
    textStyle: TextStyle = KoinTheme.typography.medium14,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    shape: Shape = RoundedCornerShape(50),
    showClickRipple: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
    chipColors: TextChipColors = TextChipDefaults.chipColors(),
    onSelect: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(shape)
            .then(
                if (showClickRipple) Modifier.clickable {
                    onSelect()
                } else Modifier.noRippleClickable {
                    onSelect()
                }
            )
            .background(if (isSelected) chipColors.selectedContainerColor else chipColors.unselectedContainerColor)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = textStyle,
            color = if (isSelected) chipColors.selectedContentColor else chipColors.unselectedContentColor
        )
    }
}


@Composable
fun keywordChipColors() = TextChipDefaults.chipColors(
    selectedContainerColor = KoinTheme.colors.primary500,
    unselectedContainerColor = KoinTheme.colors.neutral100,
    selectedContentColor = KoinTheme.colors.neutral100,
    unselectedContentColor = KoinTheme.colors.neutral500
)