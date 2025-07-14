package `in`.koreatech.koin.feature.club.ui.clubdetail.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.KoinClubEventsDropdown
import `in`.koreatech.koin.feature.club.model.EventStatus
import `in`.koreatech.koin.feature.club.model.ParcelizeClubEvent
import `in`.koreatech.koin.feature.club.model.toStringForm
import `in`.koreatech.koin.feature.club.type.EventSearchType
import `in`.koreatech.koin.feature.club.type.eventSearchTypeList
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.eventbox.DetailEventBox
import java.time.LocalDateTime
import kotlinx.collections.immutable.toPersistentList
import okhttp3.internal.immutableListOf

@Composable
fun ClubDetailEvents(
    isDropdownExpanded: Boolean,
    clubEvents: List<ParcelizeClubEvent>,
    dropdownTitle: String,
    dropdownList: List<EventSearchType>,
    modifier: Modifier = Modifier,
    showProgressBar: Boolean = false,
    isManager: Boolean = false,
    onDropdownExpandChange: (Boolean) -> Unit = {},
    onDropdownItemSelected: (Int) -> Unit = {},
    onEventCreateClick: () -> Unit = {},
    onEventClick: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 24.dp,
                end = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isManager) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                Arrangement.End
            ) {
                FilledButton(
                    text = stringResource(R.string.detail_events_create),
                    onClick = onEventCreateClick,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            KoinClubEventsDropdown(
                title = dropdownTitle,
                isDropdownExpanded = isDropdownExpanded,
                items = dropdownList.map { stringResource(it.strRes) }.toPersistentList(),
                onDropdownExpandChange = onDropdownExpandChange,
                onItemSelected = { index ->
                    onDropdownItemSelected(index)
                }
            )
        }
        if (showProgressBar) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )
            }
        } else {
            if (clubEvents.isEmpty()) {
                Spacer(Modifier.height(200.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.detail_events_empty),
                    style = KoinTheme.typography.medium18,
                    color = KoinTheme.colors.neutral500,
                    textAlign = TextAlign.Center
                )
            }
            clubEvents.forEachIndexed { index, event ->
                DetailEventBox(
                    eventId = event.id,
                    eventName = event.name,
                    imageUrl = event.imageUrls.firstOrNull() ?: "",
                    stateText = stringResource(event.status.strRes),
                    dateText = "${event.startDateTime.toStringForm()} ~ ${event.endDateTime.toStringForm()}",
                    eventIntroText = event.introduce,
                    stateColor = KoinTheme.colors.primary400,
                    isDisable = event.status == EventStatus.ENDED,
                    onClick = {
                        onEventClick(index)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubDetailEventsPreview() {
    ClubDetailEvents(
        isDropdownExpanded = false,
        clubEvents = immutableListOf(
            ParcelizeClubEvent(
                id = 1,
                name = "행사 명",
                imageUrls = immutableListOf(),
                startDateTime = LocalDateTime.now(),
                endDateTime = LocalDateTime.now().plusDays(1),
                introduce = "동아리 소개 문구",
                content = "동아리 상세 소개",
                status = EventStatus.SOON,
                isSubscribed = false
            )
        ),
        dropdownTitle = "행사 진행중",
        dropdownList = eventSearchTypeList
    )
}

@Preview(showBackground = true)
@Composable
fun ClubDetailEventsManagerPreview() {
    ClubDetailEvents(
        isDropdownExpanded = false,
        clubEvents = immutableListOf(
            ParcelizeClubEvent(
                id = 1,
                name = "행사 명",
                imageUrls = immutableListOf(),
                startDateTime = LocalDateTime.now(),
                endDateTime = LocalDateTime.now().plusDays(1),
                introduce = "동아리 소개 문구",
                content = "동아리 상세 소개",
                status = EventStatus.SOON,
                isSubscribed = false
            )
        ),
        isManager = true,
        dropdownTitle = "행사 진행중",
        dropdownList = eventSearchTypeList
    )
}
