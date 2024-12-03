package `in`.koreatech.bus.screen.timetabledetail.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShuttleTimetableDetailItem(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(
                text = "천안역",
                style = KoinTheme.typography.medium15
            )
            Text(
                text = "학화호두과자 앞",
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral500
            )
        }
        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = KoinTheme.colors.neutral300
        )
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            Row(
                modifier = Modifier.horizontalScroll(scrollState)
            ) {
                Text(
                    text = "11:10",
                    style = KoinTheme.typography.bold16,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
                Text(
                    text = "11:10",
                    style = KoinTheme.typography.bold16,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
                Text(
                    text = "11:10",
                    style = KoinTheme.typography.bold16,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
                Text(
                    text = "11:10",
                    style = KoinTheme.typography.bold16,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
                Text(
                    text = "11:10",
                    style = KoinTheme.typography.bold16,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
                Text(
                    text = "11:10",
                    style = KoinTheme.typography.bold16,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
                Text(
                    text = "11:10",
                    style = KoinTheme.typography.bold16,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableDetailItemPreview() {
    ShuttleTimetableDetailItem(
        scrollState = rememberScrollState()
    )
}