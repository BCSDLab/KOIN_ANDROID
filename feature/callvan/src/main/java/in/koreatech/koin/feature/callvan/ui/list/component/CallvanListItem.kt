package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.enums.CallvanRouteState
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanRouteCount
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanRouteDate
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanRouteInfo
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListItemClickListener
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListUiState

@Composable
fun CallvanListItem(
    uiState: CallvanListUiState,
    state: CallvanRouteState,
    modifier: Modifier = Modifier,
    clickListener: CallvanListItemClickListener = object : CallvanListItemClickListener {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 0.5.dp,
                color = KoinTheme.colors.neutral400,
                shape = KoinTheme.shapes.small
            )
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            with(uiState) {
                CallvanRouteInfo(departure = departure, destination = destination)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CallvanRouteDate(date = date, dayOfWeek = dayOfWeek, time = time)
                    Text(text = "|", color = KoinTheme.colors.neutral300)
                    CallvanRouteCount(currentCount = currentCount, maxCount = maxCount)
                }
            }
        }

        CallvanListItemButtons(
            state = state,
            clickListener = clickListener
        )
    }
}

private object PreviewData {
    const val DEPARTURE_TENNIS = "테니스장"
    const val DESTINATION_STATION = "천안역"
    const val DATE = "02.05"
    const val DAY_OF_WEEK = "월"
    const val TIME = "14:00"
    const val CURRENT_COUNT = 1
    const val MAX_COUNT = 8
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemDefaultPreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = PreviewData.DEPARTURE_TENNIS,
                destination = PreviewData.DESTINATION_STATION,
                date = PreviewData.DATE,
                dayOfWeek = PreviewData.DAY_OF_WEEK,
                time = PreviewData.TIME,
                currentCount = PreviewData.CURRENT_COUNT,
                maxCount = PreviewData.MAX_COUNT
            ),
            state = CallvanRouteState.DEFAULT
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemJoinedPreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = PreviewData.DEPARTURE_TENNIS,
                destination = PreviewData.DESTINATION_STATION,
                date = PreviewData.DATE,
                dayOfWeek = PreviewData.DAY_OF_WEEK,
                time = PreviewData.TIME,
                currentCount = PreviewData.CURRENT_COUNT,
                maxCount = PreviewData.MAX_COUNT
            ),
            state = CallvanRouteState.JOINED
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemClosedPreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = PreviewData.DEPARTURE_TENNIS,
                destination = PreviewData.DESTINATION_STATION,
                date = PreviewData.DATE,
                dayOfWeek = PreviewData.DAY_OF_WEEK,
                time = PreviewData.TIME,
                currentCount = PreviewData.CURRENT_COUNT,
                maxCount = PreviewData.MAX_COUNT
            ),
            state = CallvanRouteState.CLOSED
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemOwnerActivePreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = PreviewData.DEPARTURE_TENNIS,
                destination = PreviewData.DESTINATION_STATION,
                date = PreviewData.DATE,
                dayOfWeek = PreviewData.DAY_OF_WEEK,
                time = PreviewData.TIME,
                currentCount = PreviewData.CURRENT_COUNT,
                maxCount = PreviewData.MAX_COUNT
            ),
            state = CallvanRouteState.OWNER_ACTIVE
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemOwnerClosedPreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = PreviewData.DEPARTURE_TENNIS,
                destination = PreviewData.DESTINATION_STATION,
                date = PreviewData.DATE,
                dayOfWeek = PreviewData.DAY_OF_WEEK,
                time = PreviewData.TIME,
                currentCount = PreviewData.CURRENT_COUNT,
                maxCount = PreviewData.MAX_COUNT
            ),
            state = CallvanRouteState.OWNER_CLOSED
        )
    }
}
