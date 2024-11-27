package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.dashedBorder
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyEvent
import timber.log.Timber
import java.time.DayOfWeek

enum class TimetableEventType {
    BASIC, SELECTED, ETC_SELECTED
}

@Composable
fun TimetableEventTime(
    range: Int,
    event: TimetableEvent,
    modifier: Modifier = Modifier,
    eventType: TimetableEventType = TimetableEventType.BASIC,
    onEventTimeClick: (event: TimetableEvent) -> Unit = {}
) {
    when (eventType) {
        TimetableEventType.BASIC -> TimetableBasicEventTime(
            range = range,
            event = event,
            modifier = modifier.padding((0.5).dp),
            onEventTimeClick = onEventTimeClick
        )

        TimetableEventType.SELECTED -> TimetableSelectedEventTime(
            range = range,
            event = event,
            modifier = modifier.padding(
                start = (0.5).dp,
                top = (0.5).dp,
                end = (0.6).dp,
                bottom = (0.65).dp
            )
        )

        TimetableEventType.ETC_SELECTED -> TimetableEtcSelectedEventTime(
            range = range,
            event = event,
            modifier = modifier.padding(1.dp)
        )
    }
}


@Composable
private fun TimetableBasicEventTime(
    range: Int,
    event: TimetableEvent,
    modifier: Modifier = Modifier,
    onEventTimeClick: (event: TimetableEvent) -> Unit = {}
) {
    var height by rememberSaveable { mutableIntStateOf(0) }
    var titleMaxLine by rememberSaveable { mutableIntStateOf(Int.MAX_VALUE) }
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    var titleLineCount by rememberSaveable { mutableIntStateOf(0) }
    var contentMaxLine by rememberSaveable { mutableIntStateOf(Int.MAX_VALUE) }
    var contentHeight by rememberSaveable { mutableIntStateOf(0) }
    var contentLineCount by rememberSaveable { mutableIntStateOf(0) }

    // TODO : 1시간 30분이상일 경우 professor text 가 내려가서 보이지 않는 오류 해결해보자..
    if (height < titleHeight + contentHeight) {
        titleMaxLine = titleLineCount - 1
        contentMaxLine = if (contentLineCount == 1) {
            contentLineCount
        } else {
            contentLineCount - 1
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = event.color.content)
            .border(
                color = Color.Transparent,
                width = 1.dp,
                shape = RoundedCornerShape(
                    bottomEnd = timetableSelectedEventTimeBottomEndRound(range, event)
                )
            )
            .noRippleClickable { onEventTimeClick(event) }
            .onGloballyPositioned {
                height = it.size.height
            }
    ) {
        HorizontalDivider(color = event.color.header, thickness = 2.dp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = event.name,
            modifier = Modifier
                .padding(1.dp),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral800,
            maxLines = titleMaxLine,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                titleLineCount = it.lineCount
                titleHeight = it.size.height
            }
        )
        Text(
            text = event.professor,
            modifier = Modifier
                .padding(1.dp),
            style = KoinTheme.typography.regular10,
            color = KoinTheme.colors.neutral800,
            maxLines = contentMaxLine,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                contentLineCount = it.lineCount
                contentHeight = it.size.height
            }
        )
    }
}

@Composable
private fun TimetableSelectedEventTime(
    range: Int,
    event: TimetableEvent,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color.Transparent,
            )
            .border(
                color = KoinTheme.colors.neutral500,
                width = 1.dp,
                shape = RoundedCornerShape(
                    bottomEnd = timetableSelectedEventTimeBottomEndRound(range, event)
                )
            )
    )
}

@Composable
private fun TimetableEtcSelectedEventTime(
    range: Int,
    event: TimetableEvent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color.Transparent,
            )
            .dashedBorder(
                gapLength = 5.dp,
                color = KoinTheme.colors.neutral500,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = timetableSelectedEventTimeBottomEndRound(range, event)
                )
            )
    )
}

private fun timetableSelectedEventTimeBottomEndRound(
    range: Int,
    event: TimetableEvent
): Dp {
    if (event.dayOfWeek == DayOfWeek.FRIDAY) {
        val timeRange = if (event.end.minute == 30) {
            (event.end.hour - 9) + 1
        } else {
            (event.end.hour - 9)
        }
        if (range == timeRange) {
            return 10.dp
        }
    }
    return 0.dp
}

@Preview(showBackground = true, widthDp = 68)
@Composable
private fun TimetableEventTimePreview_Basic() {
    TimetableEventTime(
        range = 9,
        event = dummyEvent.copy(name = "dafdafdafdafdafdafdafdafdafdafdafdafdafdafdafdafdafdafdafdafdafdafdafdaf", professor = "dafdafdafdafdafdafdafdafdaf"),
        modifier = Modifier
            .sizeIn(maxHeight = (64 * 2).dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun TimetableEventTimePreview_Selected() {
    TimetableEventTime(
        range = 9,
        event = dummyEvent,
        modifier = Modifier
            .sizeIn(maxHeight = 64.dp)
            .padding(10.dp),
        eventType = TimetableEventType.SELECTED
    )
}

@Preview(showBackground = true)
@Composable
private fun TimetableEventTimePreview_Etc_Selected() {
    TimetableEventTime(
        range = 9,
        event = dummyEvent,
        modifier = Modifier
            .sizeIn(maxHeight = 64.dp)
            .padding(10.dp),
        eventType = TimetableEventType.ETC_SELECTED
    )
}