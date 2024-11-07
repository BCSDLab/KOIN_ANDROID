package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.component.TimetableEventTime
import `in`.koreatech.koin.feature.timetable.component.TimetableEventType
import `in`.koreatech.koin.feature.timetable.model.TimetableConstants
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyEvent
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

private val neutral300 = Color(0xFFE1E1E1)

@Composable
fun TimetableContent(
    range: Int,
    events: List<TimetableEvent>,
    modifier: Modifier = Modifier,
    dayHeight: Dp = 20.dp,
    timeWidth: Dp = 20.dp,
    horizontalPadding: Dp = 0.dp,
    dayCount: Int = TimetableConstants.days.size,
    height: Dp = (TimetableConstants.eventHeight).dp,
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
    onEventY: (y: Int) -> Unit = {}
) {
    val width = measureEventWidth(horizontalPadding = horizontalPadding, dayCount = dayCount)
    val initStartTime = LocalTime.of(9, 0)

    Layout(
        content = {
            TimetableConstants.days.forEach { day ->
                Box(modifier = Modifier.then(object : ParentDataModifier {
                    override fun Density.modifyParentData(parentData: Any?): String = day
                })) {
                    Text(
                        text = day,
                        style = KoinTheme.typography.medium13,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = KoinTheme.colors.neutral500
                    )
                }
            }
            repeat(range) {
                val time = (initStartTime.hour + it).toString()
                Box(modifier = Modifier.then(object : ParentDataModifier {
                    override fun Density.modifyParentData(parentData: Any?): String = time
                })) {
                    Text(
                        text = time,
                        style = KoinTheme.typography.regular12,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = KoinTheme.colors.neutral500
                    )
                }
            }
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
        modifier = modifier
            .background(Color.White)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = RoundedCornerShape(10.dp)
            )
            .drawBehind {
                repeat(range * 2 + 1) { // 가로축
                    val innerHeight = (height / 2).toPx()
                    val headerHeight = dayHeight.toPx()
                    when (it) {
                        0 -> { // 시간표 Header
                            drawLine(
                                color = neutral300,
                                start = Offset(0f, headerHeight),
                                end = Offset(size.width, headerHeight),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        else -> { // 시간표 Body
                            if (it % 2 == 1) {
                                drawLine(
                                    color = neutral300,
                                    start = Offset(
                                        timeWidth.toPx(),
                                        it * innerHeight + headerHeight
                                    ),
                                    end = Offset(size.width, it * innerHeight + headerHeight),
                                    strokeWidth = 1.dp.toPx()
                                )
                            } else {
                                drawLine(
                                    color = neutral300,
                                    start = Offset(0f, it * innerHeight + headerHeight),
                                    end = Offset(size.width, it * innerHeight + headerHeight),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                        }
                    }
                }
                repeat(dayCount + 1) { // 세로축
                    val innerWidth =
                        timeWidth.toPx() + it * ((size.width - timeWidth.toPx()) / dayCount)
                    when (it) {
                        0 -> {
                            drawLine(
                                color = neutral300,
                                start = Offset(timeWidth.toPx(), 0f),
                                end = Offset(timeWidth.toPx(), size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        else -> {
                            drawLine(
                                color = neutral300,
                                start = Offset(innerWidth, 0f),
                                end = Offset(innerWidth, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }
                }
            }
    ) { measureables, constraints ->
        val timetableWidth = width.roundToPx() * (dayCount + 1)
        val timetableHeight = height.roundToPx() * range + dayHeight.roundToPx()
        val innerWidth = (timetableWidth - timeWidth.roundToPx()) / dayCount

        // 요일 측정
        val placeablesWithDays = measureables.take(dayCount).map { measurable ->
            measurable.measure(
                constraints.copy(
                    minWidth = innerWidth, maxWidth = innerWidth,
                    minHeight = dayHeight.roundToPx(), maxHeight = dayHeight.roundToPx()
                )
            )
        }

        // 시간 측정
        val placeaablesWithTimes =
            measureables.slice((dayCount)..<(dayCount + range)).map { measurable ->
                measurable.measure(
                    constraints.copy(
                        minWidth = timeWidth.roundToPx(), maxWidth = timeWidth.roundToPx(),
                        minHeight = height.roundToPx(), maxHeight = height.roundToPx()
                    )
                )
            }


        // 강의 이벤트 박스 측정
        val placeablesWithEvents = measureables.drop(dayCount + range).map { measurable ->
            val event = measurable.parentData as TimetableEvent
            val eventDurationMinutes = ChronoUnit.MINUTES.between(event.start, event.end)
            val eventHeight = ((eventDurationMinutes / 60f) * height.toPx()).roundToInt()
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = innerWidth, maxWidth = innerWidth,
                    minHeight = eventHeight, maxHeight = eventHeight
                )
            )
            Pair(placeable, event)
        }

        layout(timetableWidth, timetableHeight) {
            // 요일 배치
            placeablesWithDays.forEachIndexed { index, placeable ->
                val dayX = index * innerWidth + timeWidth.roundToPx()
                placeable.place(dayX, 0) // 상단에 배치
            }

            // 시간 배치
            placeaablesWithTimes.forEachIndexed { index, placeable ->
                val dayY = (2 * index) * (height / 2).roundToPx() + dayHeight.roundToPx()
                placeable.place(0, dayY) // 옆단에 배치
            }

            // 강의 이벤트 박스 배치
            placeablesWithEvents.forEachIndexed { index, (placeable, event) ->
                val eventOffsetMinutes =
                    ChronoUnit.MINUTES.between(initStartTime, event.start)
                val eventY =
                    (((eventOffsetMinutes / 60f) + 0.5) * (height).toPx()).roundToInt() + (dayHeight).roundToPx() - (height / 2).roundToPx()

                val eventOffsetDays: Int = when (event.dayOfWeek) {
                    DayOfWeek.MONDAY -> 0
                    DayOfWeek.TUESDAY -> 1
                    DayOfWeek.WEDNESDAY -> 2
                    DayOfWeek.THURSDAY -> 3
                    DayOfWeek.FRIDAY -> 4
                    DayOfWeek.SATURDAY -> 5
                    DayOfWeek.SUNDAY -> 6
                    else -> -1
                }
                val eventX = eventOffsetDays * innerWidth + timeWidth.roundToPx()
                if (index == placeablesWithEvents.size - 1) {
                    onEventY(eventY)
                }
                placeable.place(eventX, eventY)
            }
        }
    }
}

@Composable
private fun measureEventWidth(horizontalPadding: Dp, dayCount: Int): Dp =
    LocalConfiguration.current.screenWidthDp.dp / (dayCount + 1) - (horizontalPadding / (dayCount + 1))

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
        horizontalPadding = 24.dp,
        events = listOf(
            dummyEvent,
            dummyEvent.copy(dayOfWeek = DayOfWeek.MONDAY),
            dummyEvent.copy(start = LocalTime.of(9, 0), dayOfWeek = DayOfWeek.WEDNESDAY),
            dummyEvent.copy(end = LocalTime.of(16, 30), dayOfWeek = DayOfWeek.THURSDAY),
            dummyEvent.copy(
                start = LocalTime.of(9, 0),
                end = LocalTime.of(16, 30),
                dayOfWeek = DayOfWeek.TUESDAY
            )
        ),
        clickEvent = listOf(dummyEvent)
    )
}