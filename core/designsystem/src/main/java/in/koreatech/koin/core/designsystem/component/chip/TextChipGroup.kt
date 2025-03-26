package `in`.koreatech.koin.core.designsystem.component.chip

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.chip.ChipOverflowStrategy.Flow
import `in`.koreatech.koin.core.designsystem.component.chip.ChipOverflowStrategy.Scroll

/**
 * 텍스트 칩 그룹
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
fun TextChipGroup(
    titles: List<String>,
    vararg selectedChipIndexes: Int,
    onChipSelected: (title: String) -> Unit,
    modifier: Modifier = Modifier,
    chipOverflowStrategy: ChipOverflowStrategy = Flow(),
    shape: Shape = RoundedCornerShape(50),
    showClickRipple: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(6.dp),
    chipColors: TextChipColors = TextChipDefaults.chipColors()
) {
    when (chipOverflowStrategy) {
        is Flow ->
            KoinTextChipFlowGroup(
                modifier = modifier,
                titles = titles,
                shape = shape,
                onChipSelected = onChipSelected,
                selectedChipIndexes = selectedChipIndexes,
                horizontalArrangement = horizontalArrangement,
                showClickRipple = showClickRipple,
                contentPadding = contentPadding,
                chipColors = chipColors,
                verticalArrangement = chipOverflowStrategy.verticalArrangement
            )
        Scroll ->
            KoinTextChipScrollGroup(
                modifier = modifier,
                titles = titles,
                shape = shape,
                onChipSelected = onChipSelected,
                selectedChipIndexes = selectedChipIndexes,
                horizontalArrangement = horizontalArrangement,
                showClickRipple = showClickRipple,
                contentPadding = contentPadding,
                chipColors = chipColors
            )
    }
}

/**
 * Chip이 화면을 넘었을 때 전략
 * @property Flow overflow된 칩들은 아래 행에 나열
 * @property Scroll 같은 행에서 계속 나열하고 스크롤 부여
 */
sealed interface ChipOverflowStrategy {
    data object Scroll : ChipOverflowStrategy

    data class Flow(
        val verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(6.dp)
    ) : ChipOverflowStrategy
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun KoinTextChipFlowGroup(
    modifier: Modifier = Modifier,
    titles: List<String>,
    shape: Shape,
    onChipSelected: (title: String) -> Unit,
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
            TextChip(
                title = it,
                isSelected = selectedChipIndexes.contains(index),
                shape = shape,
                chipColors = chipColors,
                contentPadding = contentPadding,
                showClickRipple = showClickRipple,
                onSelect = { onChipSelected(it) }
            )
        }
    }
}

@Composable
private fun KoinTextChipScrollGroup(
    modifier: Modifier = Modifier,
    titles: List<String>,
    shape: Shape,
    onChipSelected: (title: String) -> Unit,
    vararg selectedChipIndexes: Int,
    showClickRipple: Boolean,
    contentPadding: PaddingValues,
    horizontalArrangement: Arrangement.Horizontal,
    chipColors: TextChipColors
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = horizontalArrangement
    ) {
        titles.forEachIndexed { index, it ->
            TextChip(
                title = it,
                isSelected = selectedChipIndexes.contains(index),
                shape = shape,
                chipColors = chipColors,
                contentPadding = contentPadding,
                showClickRipple = showClickRipple,
                onSelect = { onChipSelected(it) }
            )
        }
    }
}

@Preview
@Composable
private fun KoinTextChipGroupFlowPreview() {
    TextChipGroup(
        titles = listOf("Chip1", "Chip2", "Chip3", "Chip4", "Chip5", "Chip6", "Chip7", "Chip8"),
        selectedChipIndexes = intArrayOf(0, 2, 3, 6),
        onChipSelected = {},
        chipOverflowStrategy = Flow(),
        chipColors = TextChipDefaults.chipColors()
    )
}

@Preview
@Composable
private fun KoinTextChipGroupScrollPreview() {
    TextChipGroup(
        titles = listOf("Chip1", "Chip2", "Chip3", "Chip4", "Chip5", "Chip6", "Chip7", "Chip8"),
        selectedChipIndexes = intArrayOf(0, 2, 3, 6),
        onChipSelected = {},
        chipOverflowStrategy = Scroll,
        chipColors = TextChipDefaults.chipColors()
    )
}
