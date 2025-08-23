package `in`.koreatech.koin.feature.dining.component.switch

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun KoinSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    switchColors: KoinSwitchColors = KoinSwitchDefaults.koinSwitchColors()
) {
    var isChecked by remember(checked) { mutableStateOf(checked) }
    val thumbOffset = remember { Animatable(if (isChecked) 1f else 0f) }

    LaunchedEffect(isChecked) {
        thumbOffset.animateTo(if (isChecked) 1f else 0f, animationSpec = tween(durationMillis = 300))
    }

    Box(
        modifier = modifier
            .height(24.dp)
            .aspectRatio(2.1f)
            .background(Color.Gray, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isChecked = !isChecked
                onCheckedChange(isChecked)
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val trackWidth = size.width
            val trackHeight = size.height
            val thumbMargin = size.height / 8
            val thumbRadius = trackHeight / 2 - thumbMargin

            // Draw track
            drawRoundRect(
                color = if (isChecked) switchColors.selectedContainerColor else switchColors.unselectedContainerColor,
                size = size,
                cornerRadius = CornerRadius(trackHeight / 2, trackHeight / 2)
            )

            // Draw thumb
            val thumbX = (thumbOffset.value * (trackWidth - (thumbRadius + thumbMargin) * 2)) + (thumbRadius + thumbMargin)
            drawCircle(
                color = if (isChecked) switchColors.selectedContentColor else switchColors.unselectedContentColor,
                radius = thumbRadius,
                center = Offset(thumbX, trackHeight / 2)
            )
        }
    }
}

object KoinSwitchDefaults {
    @Composable
    fun koinSwitchColors(
        selectedContainerColor: Color = KoinTheme.colors.primary500,
        selectedContentColor: Color = Color.White,
        unselectedContainerColor: Color = KoinTheme.colors.neutral300,
        unselectedContentColor: Color = KoinTheme.colors.neutral0
    ) = KoinSwitchColors(
        selectedContainerColor = selectedContainerColor,
        selectedContentColor = selectedContentColor,
        unselectedContainerColor = unselectedContainerColor,
        unselectedContentColor = unselectedContentColor
    )
}

class KoinSwitchColors internal constructor(
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContainerColor: Color,
    val unselectedContentColor: Color
)

@Preview
@Composable
private fun KoinSwitchCheckedPreview() {
    KoinSwitch(
        checked = true,
        onCheckedChange = {false}
    )
}

@Preview
@Composable
private fun KoinSwitchUnCheckedPreview() {
    KoinSwitch(
        checked = false,
        onCheckedChange = {true}
    )
}
