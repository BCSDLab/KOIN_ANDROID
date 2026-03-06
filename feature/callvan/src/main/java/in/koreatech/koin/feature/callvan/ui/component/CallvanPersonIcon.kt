package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanPersonIcon(
    isMine: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = RebrandKoinTheme.colors.primary500
) {
    val backgroundColor = if (isMine) tint else Color.Transparent
    val iconTint = if (isMine) KoinTheme.colors.neutral0 else tint

    Box(
        modifier = modifier
            .clip(KoinTheme.shapes.small)
            .border(1.dp, tint, KoinTheme.shapes.small)
            .background(color = backgroundColor)
            .padding(all = 4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_person),
            contentDescription = null,
            tint = iconTint
        )
    }
}

@Composable
fun callvanPersonIconColor(value: Int): Color = when (value % 8) {
    0 -> RebrandKoinTheme.colors.primary200
    1 -> Color(0xFFD39AFE)
    2 -> RebrandKoinTheme.colors.primary300
    3 -> Color(0xFFC969FC)
    4 -> RebrandKoinTheme.colors.primary400
    5 -> RebrandKoinTheme.colors.primary500
    6 -> RebrandKoinTheme.colors.primary600
    else -> RebrandKoinTheme.colors.primary700
}

@Preview
@Composable
private fun CallvanPersonIconPreview() {
    CallvanPersonIcon(
        isMine = false
    )
}

@Preview
@Composable
private fun CallvanPersonIconIsMinePreview() {
    CallvanPersonIcon(
        isMine = true
    )
}
