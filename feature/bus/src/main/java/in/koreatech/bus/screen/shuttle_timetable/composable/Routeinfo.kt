package `in`.koreatech.bus.screen.shuttle_timetable.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.getMeasuredKoreanHeightDp

@Composable
internal fun RouteInfo(
    routeName: String,
    modifier: Modifier = Modifier,
    routeDetail: String = ""
) {
    Box(
        modifier = modifier.height(KoinTheme.typography.regular14.getMeasuredKoreanHeightDp() + 16.dp),
    ) {
        Text(
            text = routeName,
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral600,
            modifier = Modifier.align(if(routeDetail.isBlank()) Alignment.Center else Alignment.TopCenter)
        )
        if(routeDetail.isNotBlank()){
            Text(
                text = routeDetail,
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral600,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
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
