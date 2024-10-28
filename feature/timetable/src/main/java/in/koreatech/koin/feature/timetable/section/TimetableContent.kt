package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.component.TimetableEventTime
import `in`.koreatech.koin.feature.timetable.component.TimetableEventType
import `in`.koreatech.koin.feature.timetable.model.TimetableConstants
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyEvent
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun TimetableContent(
    range: Int,
    dayWidth: Dp,
    events: List<TimetableEvent>,
    dayCount: Int = TimetableConstants.days.size,
    hourHeight: Dp = (TimetableConstants.eventHeight).dp,
    modifier: Modifier = Modifier,
    clickEvent: List<TimetableEvent> = emptyList(),
    content: @Composable (event: TimetableEvent, eventType: TimetableEventType, onEventTimeClick: (TimetableEvent) -> Unit) -> Unit = { event, eventType, onClick ->
        TimetableEventTime(
            event = event,
            modifier = modifier,
            eventType = eventType,
            onEventTimeClick = onClick
        )
    },
    onEventClick: (event: TimetableEvent) -> Unit = {},
) {
    Layout(
        content = {
            events.sortedBy(TimetableEvent::start).forEach { event ->
                Box(modifier = Modifier.eventData(event)) {
                    content(event, TimetableEventType.BASIC, onEventClick)
                }
            }
            if (clickEvent.isNotEmpty()) {
                clickEvent.sortedBy(TimetableEvent::start).forEach { event ->
                    Box(modifier = Modifier.eventData(event)) {
                        content(event, TimetableEventType.SELECTED, onEventClick)
                    }
                }
            }
        },
        modifier = modifier.drawBehind {
            drawLine( // ― (맨 위)
                color = Color.LightGray,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = (0.1).dp.toPx()
            )
            drawLine( // | (맨 앞)
                color = Color.LightGray,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 1.dp.toPx()
            )

            repeat(range * 2) {
                drawLine( // 가로축
                    color = Color.LightGray,
                    start = Offset(0f, (it + 1) * (hourHeight / 2).toPx()),
                    end = Offset(size.width, (it + 1) * (hourHeight / 2).toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }
            repeat(dayCount) {
                drawLine( // 세로축
                    color = Color.LightGray,
                    start = Offset((it + 1) * dayWidth.toPx(), 0f),
                    end = Offset((it + 1) * dayWidth.toPx(), size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    ) { measureables, constraints ->
        val width = dayWidth.roundToPx() * dayCount
        val height = hourHeight.roundToPx() * range

        val placeablesWithEvents = measureables.map { measurable ->
            val event = measurable.parentData as TimetableEvent
            val eventDurationMinutes = ChronoUnit.MINUTES.between(event.start, event.end)
            val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = dayWidth.roundToPx(), maxWidth = dayWidth.roundToPx(),
                    minHeight = eventHeight, maxHeight = eventHeight
                )
            )
            Pair(placeable, event)
        }

        layout(width, height) {
            placeablesWithEvents.forEach { (placeable, event) ->
                val initStartTime = LocalTime.of(9, 0)
                val eventOffsetMinutes =
                    ChronoUnit.MINUTES.between(initStartTime, event.start)
                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()

                val eventOffsetDays: Int = when (event.dayOfWeek) {
                    DayOfWeek.MONDAY -> 0
                    DayOfWeek.TUESDAY -> 1
                    DayOfWeek.WEDNESDAY -> 2
                    DayOfWeek.THURSDAY -> 3
                    DayOfWeek.FRIDAY -> 4
                    else -> -1
                }
                val eventX = eventOffsetDays * dayWidth.roundToPx()
                placeable.place(eventX, eventY)
            }
        }
    }
}

private class EventDataModifier(
    val event: TimetableEvent,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = event
}

private fun Modifier.eventData(event: TimetableEvent) =
    this.then(EventDataModifier(event))

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimetableContentPreview() {
    TimetableContent(
        range = 9,
        dayWidth = 68.dp,
        events = listOf(dummyEvent, dummyEvent.copy(dayOfWeek = DayOfWeek.MONDAY)),
    )
}