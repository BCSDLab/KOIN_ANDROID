package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.component.TimetableEventTime
import `in`.koreatech.koin.feature.timetable.component.TimetableEventType
import `in`.koreatech.koin.feature.timetable.model.TimetableConstants
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyEvent
import `in`.koreatech.koin.feature.timetable.section.TimetableContent
import `in`.koreatech.koin.feature.timetable.section.TimetableHeader
import `in`.koreatech.koin.feature.timetable.section.TimetableSidebar
import java.time.DayOfWeek


@Composable
fun Timetable(
    range: Int,
    events: List<TimetableEvent>,
    modifier: Modifier = Modifier,
    clickEvent: List<TimetableEvent> = emptyList(),
    content: @Composable
        (event: TimetableEvent, eventType: TimetableEventType, onEventTimeClick: (TimetableEvent) -> Unit) -> Unit = { event, eventType, onEventTimeClick ->
        TimetableEventTime(
            event = event,
            eventType = eventType,
            onEventTimeClick = onEventTimeClick
        )
    },
    onEventClick: (TimetableEvent) -> Unit = {},
) {
    val verticalScrollState = rememberScrollState()
    val horizontalPadding = 10 + (0.4)
    val widthPadding = String.format("%.2f", (horizontalPadding * 2) / 6).toFloat()

    val width =
        (LocalContext.current.resources.displayMetrics.widthPixels / LocalContext.current.resources.displayMetrics.density).dp / (TimetableConstants.days.size + 1) - widthPadding.dp


    Column(
        modifier = modifier
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimetableHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding.dp)
        )
        Row(
            modifier = Modifier
        ) {
            TimetableSidebar(
                modifier = Modifier
                    .verticalScroll(verticalScrollState),
                range = range,
                hourWidth = width,
            )
            TimetableContent(
                modifier = Modifier
                    .verticalScroll(verticalScrollState),
                range = range,
                dayWidth = width,
                events = events,
                clickEvent = clickEvent,
                content = content,
                onEventClick = onEventClick
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimetablePreview() {
    Timetable(
        range = 9,
        events = listOf(dummyEvent, dummyEvent.copy(dayOfWeek = DayOfWeek.MONDAY)),
        clickEvent = listOf(dummyEvent)
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