package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.component.TimetableEventTime
import `in`.koreatech.koin.feature.timetable.component.TimetableEventType
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyEvent
import `in`.koreatech.koin.feature.timetable.section.TimetableContent
import java.time.DayOfWeek


@Composable
fun Timetable(
    range: Int,
    events: List<TimetableEvent>,
    modifier: Modifier = Modifier,
    clickEvent: List<TimetableEvent> = emptyList(),
    etcClickEvent: List<TimetableEvent> = emptyList(),
    content: @Composable
        (event: TimetableEvent, eventType: TimetableEventType, onEventTimeClick: (TimetableEvent) -> Unit) -> Unit = { event, eventType, onEventTimeClick ->
        TimetableEventTime(
            range = range,
            event = event,
            modifier = Modifier,
            eventType = eventType,
            onEventTimeClick = onEventTimeClick
        )
    },
    onEventClick: (TimetableEvent) -> Unit = {},
) {
    val verticalScrollState: ScrollState = rememberScrollState()
    var scrollValue by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = scrollValue) {
        if (clickEvent.isNotEmpty()) {
            verticalScrollState.scrollTo(scrollValue)
        }
    }

    TimetableContent(
        modifier = modifier
            .verticalScroll(verticalScrollState)
            .padding(vertical = 14.dp),
        range = range,
        horizontalPadding = 48.dp,
        events = events,
        clickEvent = clickEvent,
        etcClickEvent = etcClickEvent,
        content = content,
        onEventClick = onEventClick,
        onEventY = { y ->
            scrollValue = y
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimetablePreview() {
    Timetable(
        range = 9,
        events = listOf(dummyEvent, dummyEvent.copy(dayOfWeek = DayOfWeek.MONDAY)),
        clickEvent = listOf(dummyEvent, dummyEvent.copy(dayOfWeek = DayOfWeek.THURSDAY))
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimetablePreview_Scroll() {
    Timetable(
        range = 15,
        events = listOf(dummyEvent, dummyEvent.copy(dayOfWeek = DayOfWeek.MONDAY)),
    )
}