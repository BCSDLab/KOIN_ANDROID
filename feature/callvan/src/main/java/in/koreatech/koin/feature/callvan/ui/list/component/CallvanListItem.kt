package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanParticipantCount
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanRouteInfo
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanItemState
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListItemClickListener
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListUiState
import `in`.koreatech.koin.feature.callvan.util.formatDateTime

@Composable
fun CallvanListItem(
    uiState: CallvanListUiState,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit = {},
    clickListener: CallvanListItemClickListener
) {
    val isJoined = remember(uiState.itemState) { uiState.itemState == CallvanItemState.JOINED }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 0.5.dp,
                color = KoinTheme.colors.neutral400,
                shape = KoinTheme.shapes.small
            )
            .clip(KoinTheme.shapes.small)
            .clickable(
                enabled = isJoined,
                onClick = onItemClick
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
                    Text(text = "|", color = KoinTheme.colors.neutral600)
                    CallvanParticipantCount(currentCount = currentCount, maxCount = maxCount)
                    if (isJoined) {
                        Text(text = ">", color = KoinTheme.colors.neutral600)
                    }
                }
            }
        }

        CallvanListItemButtons(
            state = uiState.itemState,
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
                id = 0,
                departure = "테니스장",
                destination = "천안 시외터미널",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8,
                itemState = CallvanItemState.DEFAULT
            ),
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
                id = 0,
                departure = "정문",
                destination = "천안 시외터미널",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8,
                itemState = CallvanItemState.JOINED
            ),
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
                id = 0,
                departure = "테니스장",
                destination = "천안역",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8,
                itemState = CallvanItemState.CLOSED
            ),
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
                id = 0,
                departure = "정문",
                destination = "천안 시외터미널",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8,
                itemState = CallvanItemState.OWNER_ACTIVE
            ),
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
                id = 0,
                departure = "정문",
                destination = "천안 시외터미널",
                date = "2025-02-05",
                time = "14:00",
                currentCount = 1,
                maxCount = 8,
                itemState = CallvanItemState.OWNER_CLOSED
            ),
            clickListener = object : CallvanListItemClickListener {}
        )
    }
}
