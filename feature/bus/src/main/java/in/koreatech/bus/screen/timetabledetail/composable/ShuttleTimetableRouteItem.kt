package `in`.koreatech.bus.screen.timetabledetail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.bus.shuttleTimetableRouteInfoMock1
import `in`.koreatech.bus.viewstate.ShuttleTimetableRouteInfoState
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun ShuttleTimetableRouteItem(
    route: ShuttleTimetableRouteInfoState,
    nodeItemHeightDp: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(IntrinsicSize.Max),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = route.name,
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral600,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = KoinTheme.colors.neutral100)
                .padding(vertical = 8.dp)
        )

        route.arrivalTimes.fastForEach { time ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .height(nodeItemHeightDp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = time,
                    style = KoinTheme.typography.bold16,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableDetailItemPreview() {
    ShuttleTimetableRouteItem(
        route = shuttleTimetableRouteInfoMock1,
        nodeItemHeightDp = 40.dp
    )
}