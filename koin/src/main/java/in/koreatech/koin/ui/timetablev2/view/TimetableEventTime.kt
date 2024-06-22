package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.model.timetable.TimetableEventType
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val timetableEventTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun TimetableEventTime(
    event: TimetableEvent,
    modifier: Modifier = Modifier,
    eventType: TimetableEventType? = null,
    onEventClick: (event: TimetableEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(bottom = 2.dp, end = 2.dp)
            .fillMaxSize()
            .background(
                color = if (eventType == TimetableEventType.SELECTED) Color.Transparent else event.color,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                color = if (eventType == TimetableEventType.SELECTED) Color.Red else Color.Transparent,
                width = if (eventType == TimetableEventType.SELECTED) 1.dp else 0.dp,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .clickable { onEventClick(event) }
    ) {
        if (eventType == TimetableEventType.BASIC) Divider(color = Color.White, thickness = 1.dp)
        Spacer(modifier = Modifier.height(2.dp))
        when (eventType) {
            TimetableEventType.BASIC -> {
                Text(
                    text = event.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 14.sp,
                )

                if (event.description != null) {
                    Text(
                        text = event.description,
                        fontSize = 8.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                }
            }

            TimetableEventType.SELECTED -> {}
            null -> {}
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableEventTimePreview() {
    val sample = TimetableEvent(
        id = 1,
        name = "관희의 수업",
        color = Color(0xFFAFBBF2),
        dayOfWeek = DayOfWeek.FRIDAY,
        start = LocalTime.of(16, 0),
        end = LocalTime.of(18, 0),
        description = "공학2관 105호",
    )

    TimetableEventTime(
        event = sample,
        eventType = TimetableEventType.BASIC,
        modifier = Modifier.sizeIn(maxHeight = 64.dp),
        onEventClick = {}
    )
}