package `in`.koreatech.bus.screen.shuttle_timetable.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.getMeasuredKoreanHeightDp

@Composable
internal fun RouteInfo(
    routeName: String,
    routeDetail: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.height(KoinTheme.typography.regular14.getMeasuredKoreanHeightDp() * 2),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (routeDetail.isBlank()) routeName else "$routeName\n$routeDetail",
            style = KoinTheme.typography.regular14.merge(
                textAlign = TextAlign.Center
            ),
            color = KoinTheme.colors.neutral600,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
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
