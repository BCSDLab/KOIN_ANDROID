package `in`.koreatech.koin.feature.club.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun KoinClubSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val backgroundColor by animateColorAsState(
        if (checked) KoinTheme.colors.primary500 else KoinTheme.colors.neutral300,
        label = "backgroundColor"
    )

    Row(
        modifier = Modifier
            .width(52.dp)
            .noRippleClickable {
                onCheckedChange(!checked)
            }
            .background(backgroundColor, KoinTheme.shapes.extraLarge)
            .clip(KoinTheme.shapes.extraLarge)
            .padding(3.dp)
    ) {
        val startPadding by animateDpAsState(
            if (checked) {
                30.dp
            } else {
                0.dp
            },
            label = "startPadding"
        )

        val endPadding by animateDpAsState(
            if (checked) {
                0.dp
            } else {
                30.dp
            },
            label = "endPadding"
        )

        Box(
            modifier = Modifier
                .padding(start = startPadding, end = endPadding)
                .size(16.dp)
                .clip(KoinTheme.shapes.extraLarge)
                .background(KoinTheme.colors.neutral0)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KoinClubSwitchPreview() {
    KoinClubSwitch(
        checked = true
    )
}
