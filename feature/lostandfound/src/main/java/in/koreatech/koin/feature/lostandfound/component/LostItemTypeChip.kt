package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory

/**
 * 분실물 종류 칩
 * @param lostItemType 분실물 종류
 * @param modifier Modifier
 */
@Composable
fun LostItemTypeChip(
    category: LostItemCategory,
    modifier: Modifier = Modifier
) {
    ReadOnlyTextChip(
        title = stringResource(category.stringRes),
        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 8.dp),
        chipColor = KoinTheme.colors.primary500,
        textColor = Color.White,
        modifier = modifier
    )
}

@Composable
fun ReadOnlyTextChip(
    title: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50),
    contentPadding: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
    chipColor: Color = KoinTheme.colors.primary500,
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
        modifier = modifier
    )
}

@Preview
@Composable
fun LostItemTypeChipPreview() {
    KoinTheme {
        LostItemTypeChip(category = LostItemCategory.NONE)
    }
}
