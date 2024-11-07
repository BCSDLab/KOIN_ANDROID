package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.icon.StableIcon
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R

@Composable
fun TimeEditBox(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(Color.White)
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = RoundedCornerShape(4.dp)
            )
            .padding((5.5).dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = KoinTheme.typography.bold12,
            color = KoinTheme.colors.neutral800,
            modifier = Modifier.padding(start = (3.5).dp)
        )

        IconButton(
            onClick = onClick,
            modifier = Modifier.size(24.dp)
        ) {
            StableIcon(
                drawableResId = R.drawable.ic_arrow_down
            )
        }

    }
}

@Preview
@Composable
fun TimeEditBoxPreview() {
    TimeEditBox("월요일")
}

@Preview
@Composable
fun TimeEditBoxPreview_time() {
    TimeEditBox("09:00")
}
