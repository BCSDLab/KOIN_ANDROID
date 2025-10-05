package `in`.koreatech.bus.screen.shuttle_timetable.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
internal fun RouteInfo(
    routeName: String,
    modifier: Modifier = Modifier,
    routeDetail: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = routeName,
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral600,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = routeDetail,
            style = KoinTheme.typography.regular10,
            color = KoinTheme.colors.neutral600,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun NodeItemPreview() {
    RouteInfo(
        routeName = "1회",
        routeDetail = "토요일"
    )
}

@Composable
@Preview(showBackground = true)
private fun NodeItemNoDescriptionPreview() {
    RouteInfo(
        routeName = "1회",
        routeDetail = ""
    )
}
