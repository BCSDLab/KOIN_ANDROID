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
            modifier = modifier,
            onEventTimeClick = onEventTimeClick
        )

        TimetableEventType.SELECTED -> TimetableSelectedEventTime(
            modifier = modifier
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
            .padding(end = 2.dp, bottom = 2.dp)
            .background(
                color = event.color,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .clickable { onEventTimeClick(event) }
    ) {
        HorizontalDivider(color = Color.White, thickness = 1.dp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = event.timeDashText(),
            fontSize = 8.sp,
            color = Color.White
        )
        Text(
            text = event.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        if (event.description != null) {
            Text(
                text = event.description,
                fontSize = 8.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TimetableSelectedEventTime(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(end = 2.dp, bottom = 2.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                color = Color.Red,
                width = 1.dp,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun TimetableEventTimePreview_Basic() {
    TimetableEventTime(
        event = dummyEvent,
        modifier = Modifier.sizeIn(maxHeight = 64.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun TimetableEventTimePreview_Selected() {
    TimetableEventTime(
        event = dummyEvent,
        modifier = Modifier.sizeIn(maxHeight = 64.dp),
        eventType = TimetableEventType.SELECTED
    )
}