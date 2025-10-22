package `in`.koreatech.koin.feature.store.review.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

data class MenuTagChipStyle(
    val textColor: Color,
    val textStyle: TextStyle,
    val containerColor: Color,
    val borderWidth: Dp,
    val borderColor: Color,
    val shape: Shape,
    val paddingValues: PaddingValues
)

data class MenuTagChipIconStyle(
    val iconColor: Color,
    val iconSize: Dp
)

object MenuTagChipDefaults {
    @Composable
    fun menuTagChipStyle(
        textColor: Color = RebrandKoinTheme.colors.primary300,
        textStyle: TextStyle = RebrandKoinTheme.typography.regular12,
        containerColor: Color = RebrandKoinTheme.colors.neutral0,
        borderWidth: Dp = 1.dp,
        borderColor: Color = RebrandKoinTheme.colors.primary300,
        shape: Shape = RebrandKoinTheme.shapes.extraSmall.copy(all = CornerSize(5.dp)),
        paddingValues: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 3.dp)
    ): MenuTagChipStyle = MenuTagChipStyle(
        textColor = textColor,
        textStyle = textStyle,
        containerColor = containerColor,
        borderWidth = borderWidth,
        borderColor = borderColor,
        shape = shape,
        paddingValues = paddingValues
    )

    @Composable
    fun menuTagChipIconStyle(
        iconColor: Color = RebrandKoinTheme.colors.primary300,
        iconSize: Dp = 14.dp
    ): MenuTagChipIconStyle = MenuTagChipIconStyle(
        iconColor = iconColor,
        iconSize = iconSize
    )
}

@Composable
fun MenuTagChipGroup(
    menuTags: List<String>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = { }
) {
    FlowRow(
        modifier = modifier
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        menuTags.forEachIndexed { i, tag ->
            MenuTagChip(
                text = tag,
                trailingIcon = painterResource(id = R.drawable.ic_close_thin),
                onClick = { onClick(i) }
            )
        }
    }
}

@Composable
fun MenuTagChip(
    text: String,
    modifier: Modifier = Modifier,
    chipStyle: MenuTagChipStyle = MenuTagChipDefaults.menuTagChipStyle(),
    trailingIcon: Painter? = null,
    trailingIconStyle: MenuTagChipIconStyle = MenuTagChipDefaults.menuTagChipIconStyle(),
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .border(width = chipStyle.borderWidth, color = chipStyle.borderColor, shape = chipStyle.shape)
            .clip(shape = chipStyle.shape)
            .background(color = chipStyle.containerColor, shape = chipStyle.shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(chipStyle.paddingValues),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = chipStyle.textStyle,
                color = chipStyle.textColor
            )
            Spacer(modifier = Modifier.width(4.dp))
            if (trailingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(trailingIconStyle.iconSize)
                        .paint(
                            painter = trailingIcon,
                            colorFilter = ColorFilter.tint(trailingIconStyle.iconColor)
                        )
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun MenuTagChipGroupPreview() {
    val tagList = listOf(
        "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "닭발", "계란찜", "1인 매운 닭발",
        "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발"
    )

    RebrandKoinTheme {
        MenuTagChipGroup(
            menuTags = tagList,
            modifier = Modifier,
            onClick = {}
        )
    }
}
