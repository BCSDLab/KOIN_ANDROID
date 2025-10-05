package `in`.koreatech.koin.ui.developer.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DeveloperSettingItem(
    title: String,
    key: String,
    value: Boolean,
    modifier: Modifier = Modifier,
    description: String? = null,
    onValueChange: (String, Boolean) -> Unit = { _, _ -> }
) {
    val backgroundColor by animateColorAsState(
        if (value) KoinTheme.colors.primary500 else KoinTheme.colors.neutral300,
        label = "backgroundColor"
    )

    Row(
        modifier = modifier.noRippleClickable {
            onValueChange(key, !value)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            BasicText(
                text = title,
                style = KoinTheme.typography.regular16
            )

            if (description != null) {
                BasicText(
                    text = description,
                    style = KoinTheme.typography.regular13
                )
            }
        }

        Row(
            modifier = Modifier
                .background(backgroundColor, KoinTheme.shapes.extraLarge)
                .clip(KoinTheme.shapes.extraLarge)
                .padding(3.dp)
        ) {
            val startPadding by animateDpAsState(
                if (value) {
                    24.dp
                } else {
                    0.dp
                },
                label = "startPadding"
            )

            val endPadding by animateDpAsState(
                if (value) {
                    0.dp
                } else {
                    24.dp
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
}
