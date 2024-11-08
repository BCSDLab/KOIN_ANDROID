package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.util.pxToDp
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.feature.timetable.component.CircleLoadingBar
import `in`.koreatech.koin.feature.timetable.component.TimetableDownloadBox
import `in`.koreatech.koin.feature.timetable.component.TimetableScheduleBox
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyLecture
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetableScreen(
    loading: Boolean,
    range: Int,
    lectures: List<Lecture>,
    semesters: List<String>,
    currentSemester: String,
    selectedLecture: Lecture?,
    searchText: String,
    bottomSheetContentMode: TimetableBottomSheetContentMode,
    sheetState: BottomSheetState,
    scaffoldState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
    timetableEvents: List<TimetableEvent> = emptyList(),
    clickedTimetableEvents: List<TimetableEvent> = emptyList(),
    etcClickedTimetableEvents: List<TimetableEvent> = emptyList(),
    onSearchTextChange: (text: String) -> Unit = {},
    onClickTimetableSchedule: () -> Unit = {},
    onClickDownloadTimetable: () -> Unit = {},
    onClickAddLecture: (lecture: Lecture) -> Unit = {},
    onClickRemoveLecture: (lecture: Lecture) -> Unit = {},
    onClickLecture: (timetableEvents: List<TimetableEvent>) -> Unit = {},
    onSelectedLecture: (lecture: Lecture?) -> Unit = {},
    onClickSettingIcon: () -> Unit = {},
    onClickSearchIcon: () -> Unit = {},
    onClickTimetableEvent: (event: TimetableEvent) -> Unit = {},
    onClickAddLectureMode: () -> Unit = {},
    onClickAddCustomLectureMode: () -> Unit = {},
) {
    var bottomSheetHeight by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {
            TimetableBottomSheet(
                lectures = lectures,
                selectedLecture = selectedLecture,
                searchText = searchText,
                bottomSheetContentMode = bottomSheetContentMode,
                timetableEvents = timetableEvents,
                onClickAddLectureMode = onClickAddLectureMode,
                onClickAddCustomLectureMode = onClickAddCustomLectureMode,
                onComplete = { scope.launch { sheetState.collapse() } },
                onClickSettingIcon = onClickSettingIcon,
                onClickSearchIcon = onClickSearchIcon,
                onSearchTextChange = onSearchTextChange,
                onClickAddLecture = onClickAddLecture,
                onClickRemoveLecture = onClickRemoveLecture,
                onClickLecture = onClickLecture,
                onSelectedLecture = onSelectedLecture,
                onBottomSheetHeightChange = { bottomSheetHeight = it },
            )
        },
        sheetPeekHeight = 0.dp,
        sheetElevation = 20.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .dynamicPadding(sheetState, bottomSheetHeight),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TimetableScheduleBox(onClick = onClickTimetableSchedule)
                TimetableDownloadBox(onClick = onClickDownloadTimetable)
            }
            Timetable(
                range = range,
                modifier = Modifier,
                events = timetableEvents,
                clickEvent = clickedTimetableEvents,
                etcClickEvent = etcClickedTimetableEvents,
                onEventClick = onClickTimetableEvent
            )
        }
        CircleLoadingBar(loading = loading)
    }
}


@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.dynamicPadding(
    sheetState: BottomSheetState,
    sheetHeight: Float,
) = padding(
    bottom = if (sheetState.isExpanded) {
        if (sheetState.progress == 1.0f) {
            if (sheetState.currentValue == BottomSheetValue.Expanded && sheetState.targetValue == BottomSheetValue.Collapsed) {
                0.dp
            } else {
                sheetHeight.pxToDp
            }
        } else {
            sheetHeight.pxToDp * (1.0f - sheetState.progress)
        }
    } else {
        0.dp
    }
)

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimetableScreenPreview() {
    TimetableScreen(
        loading = false,
        range = 9,
        lectures = listOf(dummyLecture),
        semesters = listOf("20242"),
        currentSemester = "20242",
        selectedLecture = null,
        searchText = "",
        bottomSheetContentMode = TimetableBottomSheetContentMode.BASIC,
        sheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        ),
        scaffoldState = rememberBottomSheetScaffoldState()
    )
}