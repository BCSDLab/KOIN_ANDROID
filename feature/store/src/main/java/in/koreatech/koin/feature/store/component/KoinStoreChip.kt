package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
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

class KoinStoreChipStyle(
    val textColor: Color,
    val textStyle: TextStyle,
    val containerColor: Color,
    val elevation: Dp,
    val ambientColor: Color,
    val spotColor: Color,
    val borderWidth: Dp,
    val borderColor: Color,
    val shape: Shape,
    val paddingValues: PaddingValues,
    val innerPadding: Dp
)

class KoinStoreChipIconStyle(
    val iconColor: Color,
    val iconSize: Dp
)

object KoinStoreChipDefaults {
    @Composable
    fun koinStoreChipStyle(
        textColor: Color = RebrandKoinTheme.colors.neutral400,
        textStyle: TextStyle = RebrandKoinTheme.typography.medium14,
        containerColor: Color = RebrandKoinTheme.colors.neutral0,
        elevation: Dp = 4.dp,
        ambientColor: Color = RebrandKoinTheme.colors.neutral400,
        spotColor: Color = RebrandKoinTheme.colors.neutral500,
        borderWidth: Dp = 0.dp,
        borderColor: Color = containerColor,
        shape: Shape = RoundedCornerShape(24.dp),
        paddingValues: PaddingValues = PaddingValues(vertical = 6.dp, horizontal = 8.dp),
        innerPadding: Dp = 6.dp
    ): KoinStoreChipStyle = KoinStoreChipStyle(
        textColor = textColor,
        textStyle = textStyle,
        containerColor = containerColor,
        elevation = elevation,
        ambientColor = ambientColor,
        spotColor = spotColor,
        borderWidth = borderWidth,
        borderColor = borderColor,
        shape = shape,
        paddingValues = paddingValues,
        innerPadding = innerPadding
    )

    @Composable
    fun koinStoreIconStyle(
        iconColor: Color = RebrandKoinTheme.colors.neutral400,
        iconSize: Dp = 16.dp
    ): KoinStoreChipIconStyle = KoinStoreChipIconStyle(
        iconColor = iconColor,
        iconSize = iconSize
    )
}

@Composable
fun KoinStoreChip(
    text: String,
    modifier: Modifier = Modifier,
    chipStyle: KoinStoreChipStyle = KoinStoreChipDefaults.koinStoreChipStyle(),
    leadingIcon: Painter? = null,
    leadingIconStyle: KoinStoreChipIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(),
    trailingIcon: Painter? = null,
    trailingIconStyle: KoinStoreChipIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(),
    enabled: Boolean = true,
    enableRipple: Boolean = true,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .shadow(
                elevation = chipStyle.elevation,
                shape = chipStyle.shape,
                ambientColor = chipStyle.ambientColor,
                spotColor = chipStyle.spotColor
            )
            .border(chipStyle.borderWidth, chipStyle.borderColor, chipStyle.shape)
            .clip(chipStyle.shape)
            .background(chipStyle.containerColor, shape = chipStyle.shape)
            .clickable(
                interactionSource = interactionSource,
                indication = if (enableRipple) LocalIndication.current else null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(chipStyle.paddingValues),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Box(
                modifier = Modifier
                    .size(leadingIconStyle.iconSize)
                    .paint(
                        painter = leadingIcon,
                        colorFilter = ColorFilter.tint(leadingIconStyle.iconColor)
                    )
            )

            Spacer(modifier = Modifier.width(chipStyle.innerPadding))
        }

        BasicText(
            text = text,
            style = chipStyle.textStyle.merge(
                color = chipStyle.textColor
            )
        )

        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(chipStyle.innerPadding))

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

@Preview(showBackground = true)
@Composable
private fun KoinStoreFilterChipPreview() {
    KoinStoreChip(
        text = "한식",
        leadingIcon = painterResource(id = R.drawable.ic_bcsd_symbol)
    )
}

@Preview(showBackground = true)
@Composable
private fun KoinStoreFilterChipSelectedPreview() {
    KoinStoreChip(
        text = "한식",
        leadingIcon = painterResource(id = R.drawable.ic_bcsd_symbol),
        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
            elevation = 0.dp,
            containerColor = RebrandKoinTheme.colors.primary500,
            textColor = RebrandKoinTheme.colors.neutral0
        ),
        leadingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
            iconColor = RebrandKoinTheme.colors.neutral0
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun KoinStoreOrderChipPreview() {
    KoinStoreChip(
        text = "Test",
        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
            textColor = RebrandKoinTheme.colors.primary500,
            borderWidth = 1.dp,
            borderColor = RebrandKoinTheme.colors.primary500,
            elevation = 0.dp
        ),
        trailingIcon = painterResource(R.drawable.ic_store_arrow_down),
        trailingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
            iconColor = RebrandKoinTheme.colors.primary500
        )
    )
}
