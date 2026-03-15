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
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanItemState
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanParticipantCount
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanRouteInfo
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListItemClickListener
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListUiState
import `in`.koreatech.koin.feature.callvan.util.formatDateTime

@Composable
fun CallvanListItem(
    uiState: CallvanListUiState,
    state: CallvanItemState,
    modifier: Modifier = Modifier,
    clickListener: CallvanListItemClickListener
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
                    Text(
                        text = formatDateTime(date, time),
                        style = KoinTheme.typography.regular12,
                        color = KoinTheme.colors.neutral600
                    )
                    Text(text = "|", color = KoinTheme.colors.neutral300)
                    CallvanParticipantCount(currentCount = currentCount, maxCount = maxCount)
                }
            }
        }

        CallvanListItemButtons(
            state = state,
            clickListener = clickListener
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemDefaultPreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = "테니스장",
                destination = "천안 시외터미널",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8
            ),
            state = CallvanItemState.DEFAULT,
            clickListener = object : CallvanListItemClickListener {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemJoinedPreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = "정문",
                destination = "천안 시외터미널",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8
            ),
            state = CallvanItemState.JOINED,
            clickListener = object : CallvanListItemClickListener {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemClosedPreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = "테니스장",
                destination = "천안역",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8
            ),
            state = CallvanItemState.CLOSED,
            clickListener = object : CallvanListItemClickListener {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemOwnerActivePreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = "정문",
                destination = "천안 시외터미널",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8
            ),
            state = CallvanItemState.OWNER_ACTIVE,
            clickListener = object : CallvanListItemClickListener {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListItemOwnerClosedPreview() {
    RebrandKoinTheme {
        CallvanListItem(
            uiState = CallvanListUiState(
                departure = "정문",
                destination = "천안 시외터미널",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8
            ),
            state = CallvanItemState.OWNER_CLOSED,
            clickListener = object : CallvanListItemClickListener {}
        )
    }
}