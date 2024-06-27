package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
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
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.model.timetable.TimetableEventType
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun TimetableContent(
    dayWidth: Dp,
    hourHeight: Dp,
    events: List<TimetableEvent>,
    modifier: Modifier = Modifier,
    clickEvent: List<TimetableEvent> = emptyList(),
    onEventY: (Int) -> Unit,
    onEventClick: (event: TimetableEvent) -> Unit,
    eventContent: @Composable (event: TimetableEvent, eventType: TimetableEventType, onEventClick: (TimetableEvent) -> Unit) -> Unit = { event, eventType, onClick ->
        TimetableEventTime(event = event, eventType = eventType, onEventClick = onClick)
    },
) {
    val days = 5
    val dividerColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    val times = 15

    Layout(
        content = {
            events.sortedBy(TimetableEvent::start).forEach { event ->
                Box(modifier = Modifier.eventData(event)) {
                    eventContent(event, TimetableEventType.BASIC, onEventClick = onEventClick)
                }
            }
            if (clickEvent.isNotEmpty()) {
                clickEvent.sortedBy(TimetableEvent::start).forEach { event ->
                    Box(modifier = Modifier.eventData(event)) {
                        eventContent(
                            event,
                            TimetableEventType.SELECTED,
                            onEventClick = onEventClick
                        )
                    }
                }
            }
        },
        modifier = modifier.drawBehind {
            drawLine(
                dividerColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                dividerColor,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 1.dp.toPx()
            )

            repeat(times * 2) {
                drawLine(
                    dividerColor,
                    start = Offset(0f, (it + 1) * (hourHeight / 2).toPx()),
                    end = Offset(size.width, (it + 1) * (hourHeight / 2).toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }
            repeat(days - 1) {
                drawLine(
                    dividerColor,
                    start = Offset((it + 1) * dayWidth.toPx(), 0f),
                    end = Offset((it + 1) * dayWidth.toPx(), size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    ) { measureables, constraints ->
        val height = hourHeight.roundToPx() * 15
        val width = dayWidth.roundToPx() * days
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
            placeablesWithEvents.forEachIndexed { index, (placeable, event) ->
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
                if (index == placeablesWithEvents.size - 1) {
                    onEventY(eventY)
                }
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

@Preview(showBackground = true)
@Composable
private fun TimetableContentPreview() {
    val samples = listOf(
        TimetableEvent(
            id = 1,
            name = "관희의 수업1",
            color = Color(0xFFAFBBF2),
            dayOfWeek = DayOfWeek.FRIDAY,
            start = LocalTime.of(16, 0),
            end = LocalTime.of(18, 0),
            description = "공학2관 101호",
        ),
        TimetableEvent(
            id = 2,
            name = "관희의 수업2",
            color = Color(0xFFDEE4FF),
            dayOfWeek = DayOfWeek.THURSDAY,
            start = LocalTime.of(14, 0),
            end = LocalTime.of(16, 0),
            description = "공학2관 105호",
        )
    )

    TimetableContent(
        events = samples,
        dayWidth = 68.dp,
        hourHeight = 64.dp,
        onEventY = {},
        onEventClick = {}
    )
}