package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory

/**
 * 분실물 종류 칩
 * @param lostItemType 분실물 종류
 * @param modifier Modifier
 */

object LostItemTypeChipDefaults {
    @Composable
    fun colors(
        chipColor: Color = RebrandKoinTheme.colors.primary100,
        textColor: Color = RebrandKoinTheme.colors.primary600
    ) = LostItemTypeChipColors(
        chipColor = chipColor,
        textColor = textColor
    )

    @Composable
    fun blueColors() = colors(
        chipColor = Color(0xFFE4F2FF),
        textColor = Color(0xFF3A70E2)
    )

    @Composable
    fun greenColors() = colors(
        chipColor = Color(0xFFE5F5EC),
        textColor = Color(0xFF0C9D61)
    )

    @Composable
    fun yellowColors() = colors(
        chipColor = Color(0xFFFFF9EE),
        textColor = Color(0xFFFFAD0D)
    )

    @Composable
    fun redColors() = colors(
        chipColor = Color(0xFFFFEBEE),
        textColor = Color(0xFFF64C4C)
    )
}

data class LostItemTypeChipColors(
    val chipColor: Color,
    val textColor: Color
)

@Composable
fun LostItemTypeChip(
    category: LostItemCategory,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = RebrandKoinTheme.typography.medium14,
    chipColors: LostItemTypeChipColors = LostItemTypeChipDefaults.colors()
) {
    ReadOnlyTextChip(
        title = stringResource(category.stringRes),
        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 8.dp),
        chipColor = chipColors.chipColor,
        textStyle = textStyle,
        textColor = chipColors.textColor,
        modifier = modifier
    )
}

@Composable
fun LostItemTypeChip(
    category: LostItemCategory,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = RebrandKoinTheme.typography.medium14
) {
    val colors = when (category) {
        LostItemCategory.CARD -> LostItemTypeChipDefaults.greenColors()
        LostItemCategory.ID -> LostItemTypeChipDefaults.yellowColors()
        LostItemCategory.WALLET -> LostItemTypeChipDefaults.colors()
        LostItemCategory.ELECTRONIC_DEVICE -> LostItemTypeChipDefaults.blueColors()
        LostItemCategory.OTHER -> LostItemTypeChipDefaults.redColors()
        LostItemCategory.NONE -> LostItemTypeChipDefaults.colors()
    }

    LostItemTypeChip(
        category = category,
        textStyle = textStyle,
        modifier = modifier,
        chipColors = colors
    )
}

@Composable
fun ReadOnlyTextChip(
    title: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50),
    textStyle: TextStyle = RebrandKoinTheme.typography.medium14,
    contentPadding: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
    chipColor: Color = RebrandKoinTheme.colors.primary500,
    textColor: Color = Color.White
) {
    LostAndFoundTextChip(
        title = title,
        isSelected = false,
        shape = shape,
        showClickRipple = false,
        onSelect = {},
        contentPadding = contentPadding,
        chipColors = TextChipDefaults.chipColors(
            selectedContainerColor = chipColor,
            unselectedContainerColor = chipColor,
            selectedContentColor = textColor,
            unselectedContentColor = textColor
        ),
        textStyle = textStyle,
        modifier = modifier
    )
}

@Preview
@Composable
private fun LostItemTypeChipPreview() {
    RebrandKoinTheme {
        LostItemTypeChip(category = LostItemCategory.NONE)
    }
}
