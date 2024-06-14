package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.model.timetable.TimetableEventType
import `in`.koreatech.koin.util.ext.pxToDp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Timetable(
    events: List<TimetableEvent>,
    modifier: Modifier = Modifier,
    clickEvent: List<TimetableEvent> = emptyList(),
    sheetState: BottomSheetState = BottomSheetState(
        BottomSheetValue.Collapsed,
        density = Density(1f)
    ),
    onEventClick: (TimetableEvent) -> Unit,
    eventContent: @Composable
        (event: TimetableEvent, eventType: TimetableEventType?, onEventClick: (TimetableEvent) -> Unit) -> Unit = { event, eventType, onEventClick ->
        TimetableEventTime(event = event, eventType = eventType, onEventClick = onEventClick)
    },
) {
    val days = 5
    val dayWidth = 68.dp
    val hourSidebarWidth =
        (LocalContext.current.resources.displayMetrics.widthPixels / LocalContext.current.resources.displayMetrics.density).dp - (dayWidth * days)
    val hourHeight = 64.dp
    val verticalScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .background(Color.White)
            .padding(
                /**
                 * 바텀 시트 올라올 때,
                 * 바텀 시트 크기 만큼 Bottom Padding 주기
                 */
                bottom = if (sheetState.isExpanded) {
                    if (sheetState.currentValue == BottomSheetValue.Expanded) {
                        if (sheetState.targetValue == BottomSheetValue.Expanded && sheetState.progress == 1f) {
                            sheetState.requireOffset().pxToDp
                        } else {
                            0.dp
                        }
                    } else {
                        0.dp
                    }
                } else {
                    0.dp
                }
            )
    ) {
        TimetableHeader(
            modifier = Modifier.fillMaxWidth(),
            dayStartPadding = hourSidebarWidth,
        )
        Row(
            modifier = Modifier
                .weight(1f)
        ) {
            TimetableSidebar(
                modifier = Modifier
                    .verticalScroll(verticalScrollState),
                hourHeight = hourHeight,
                hourWidth = hourSidebarWidth,
            )
            TimetableContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScrollState),
                clickEvent = clickEvent,
                eventContent = eventContent,
                events = events,
                dayWidth = dayWidth,
                hourHeight = hourHeight,
                onEventClick = onEventClick
            )
        }
    }
}
