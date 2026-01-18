package `in`.koreatech.koin.feature.article.ui.lostandfound.detail.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun LostAndFoundDetailCustomSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    val thumbOffset by animateDpAsState(targetValue = if (checked) 18.dp else 2.dp)
    val backgroundColor by animateColorAsState(targetValue = if (checked) KoinTheme.colors.success400 else KoinTheme.colors.neutral300)

    Box(
        modifier = modifier
            .size(width = 40.dp, height = 22.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(16.dp)
                .clip(CircleShape)
                .background(KoinTheme.colors.neutral0)
        )
    }
}

@Preview
@Composable
private fun LostAndFoundDetailCustomSwitchPreview() {
    LostAndFoundDetailCustomSwitch(
        checked = true,
        onCheckedChange = {}
    )
}
