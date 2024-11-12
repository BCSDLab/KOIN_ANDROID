package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyEvent

enum class TimetableEventType {
    BASIC, SELECTED
}

@Composable
fun TimetableEventTime(
    event: TimetableEvent,
    modifier: Modifier = Modifier,
    eventType: TimetableEventType = TimetableEventType.BASIC,
    onEventTimeClick: (event: TimetableEvent) -> Unit = {}
) {
    when (eventType) {
        TimetableEventType.BASIC -> TimetableBasicEventTime(
            event = event,
            modifier = modifier.padding((0.5).dp),
            onEventTimeClick = onEventTimeClick
        )

        TimetableEventType.SELECTED -> TimetableSelectedEventTime(
            modifier = modifier.padding(start = (0.5).dp , top = (0.5).dp, end = (0.6).dp, bottom = (0.65).dp)
        )
    }
}

@Composable
private fun TimetableBasicEventTime(
    event: TimetableEvent,
    modifier: Modifier = Modifier,
    onEventTimeClick: (event: TimetableEvent) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = event.color.content)
            .clickable { onEventTimeClick(event) }
    ) {
        HorizontalDivider(color = event.color.header, thickness = 2.dp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = event.name,
            modifier = Modifier.padding(1.dp),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral800
        )
        Text(
            text = event.description.orEmpty(),
            modifier = Modifier.padding(1.dp),
            style = KoinTheme.typography.regular10,
            color = KoinTheme.colors.neutral800
        )
    }
}

@Composable
private fun TimetableSelectedEventTime(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
            .border(
                color = KoinTheme.colors.neutral500,
                width = 1.dp,
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun TimetableEventTimePreview_Basic() {
    TimetableEventTime(
        event = dummyEvent,
        modifier = Modifier
            .sizeIn(maxHeight = 64.dp)
            .padding(10.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun TimetableEventTimePreview_Selected() {
    TimetableEventTime(
        event = dummyEvent,
        modifier = Modifier
            .sizeIn(maxHeight = 64.dp)
            .padding(10.dp),
        eventType = TimetableEventType.SELECTED
    )
}