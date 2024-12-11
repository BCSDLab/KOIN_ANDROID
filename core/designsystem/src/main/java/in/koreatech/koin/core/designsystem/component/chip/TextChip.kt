package `in`.koreatech.koin.core.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * 텍스트 칩
 * @param title 텍스트
 * @param isSelected 선택 여부
 * @param shape 칩 모양
 * @param showClickRipple 클릭시 리플 효과 표시 여부
 * @param onSelect 클릭시 실행할 함수
 * @param contentPadding 칩 내부 padding
 * @param chipColors 칩 색상
 */
@Composable
fun TextChip(
    title: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    shape: Shape = RoundedCornerShape(50),
    showClickRipple: Boolean = true,
    onSelect: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
    chipColors: TextChipColors = TextChipDefaults.chipColors(),
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
            .background(if(isSelected) chipColors.selectedContainerColor else chipColors.unselectedContainerColor)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) chipColors.selectedContentColor else chipColors.unselectedContentColor,
        )
    }
}

object TextChipDefaults {

    @Composable
    fun chipColors(
        selectedContainerColor: Color = KoinTheme.colors.primary500,
        selectedContentColor: Color = Color.White,
        unselectedContainerColor: Color = KoinTheme.colors.neutral100,
        unselectedContentColor: Color = KoinTheme.colors.neutral500,
    ) = TextChipColors(
        selectedContainerColor = selectedContainerColor,
        selectedContentColor = selectedContentColor,
        unselectedContainerColor = unselectedContainerColor,
        unselectedContentColor = unselectedContentColor,
    )
}

class TextChipColors internal constructor(
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContainerColor: Color,
    val unselectedContentColor: Color,
)

@Preview
@Composable
private fun KoinTextChipPreview() {
    TextChip(
        title = "주중노선",
        isSelected = false,
    )
}

@Preview
@Composable
private fun KoinTextChipSelectedPreview() {
    TextChip(
        title = "주말노선",
        isSelected = true,
    )
}