package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.util.pxToDp
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.feature.timetable.component.TimetableDownloadBox
import `in`.koreatech.koin.feature.timetable.component.TimetableScheduleBox
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyEvent
import `in`.koreatech.koin.feature.timetable.model.dummyLecture
import `in`.koreatech.koin.feature.timetable.state.BottomSheetUI
import `in`.koreatech.koin.feature.timetable.state.CustomContentState
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState
import java.time.DayOfWeek

@Composable
fun TimetableScreen(
    range: Int,
    lectures: List<Lecture>,
    detailLecture: TimetableLecture?,
    semesters: List<String>,
    currentSemester: String,
    timetableName: String,
    customContents: CustomContentState,
    selectedLecture: Lecture?,
    searchText: String,
    bottomSheetContentMode: TimetableBottomSheetContentMode,
    bottomSheetUI: BottomSheetUI,
    sheetState: BottomSheetState,
    sheetLazyListState: LazyListState,
    scaffoldState: BottomSheetScaffoldState,
    graphicsLayer: GraphicsLayer = rememberGraphicsLayer(),
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
    onClickSettingIcon: (visible: Boolean) -> Unit = {},
    onClickSearchIcon: () -> Unit = {},
    onClickTimetableEvent: (event: TimetableEvent) -> Unit = {},
    onClickAddLectureMode: (mode: TimetableBottomSheetContentMode) -> Unit = {},
    onClickAddCustomLectureMode: () -> Unit = {},
    onClickBottomSheetComplete: () -> Unit = {},
    onClickBottomSheetDetailComplete: () -> Unit = {},
    onClickBottomSheetDetailDelete: (TimetableLecture) -> Unit = {},
    onScheduleNameChange: (text: String) -> Unit = {},
    onProfessorNameChange: (text: String) -> Unit = {},
    onExtraPlaceNameChange: (id: Int, text: String) -> Unit = { _, _ -> },
    onDayOfWeekChange: (content: CustomExtraContentState) -> Unit = { },
    onClickStartTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickEndTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickAddCustomContent: () -> Unit = {},
    onClickRemoveCustomContent: (id: Int) -> Unit = {}
) {
    var bottomSheetHeight by remember { mutableFloatStateOf(0f) }

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetGesturesEnabled = !sheetLazyListState.isScrollInProgress,
        sheetContent = {
            when (bottomSheetUI) {
                BottomSheetUI.DEFAULT -> {
                    TimetableBottomSheet(
                        lectures = lectures,
                        selectedLecture = selectedLecture,
                        searchText = searchText,
                        customContents = customContents,
                        bottomSheetContentMode = bottomSheetContentMode,
                        timetableEvents = timetableEvents,
                        sheetLazyListState = sheetLazyListState,
                        onClickAddLectureMode = onClickAddLectureMode,
                        onClickAddCustomLectureMode = onClickAddCustomLectureMode,
                        onComplete = onClickBottomSheetComplete,
                        onClickSettingIcon = onClickSettingIcon,
                        onClickSearchIcon = onClickSearchIcon,
                        onSearchTextChange = onSearchTextChange,
                        onClickAddLecture = onClickAddLecture,
                        onClickRemoveLecture = onClickRemoveLecture,
                        onClickLecture = onClickLecture,
                        onSelectedLecture = onSelectedLecture,
                        onBottomSheetHeightChange = { bottomSheetHeight = it },
                        onScheduleNameChange = onScheduleNameChange,
                        onProfessorNameChange = onProfessorNameChange,
                        onExtraPlaceNameChange = onExtraPlaceNameChange,
                        onDayOfWeekChange = onDayOfWeekChange,
                        onClickStartTime = onClickStartTime,
                        onClickEndTime = onClickEndTime,
                        onClickAddCustomContent = onClickAddCustomContent,
                        onClickRemoveCustomContent = onClickRemoveCustomContent
                    )
                }

                BottomSheetUI.DETAIL -> {
                    LectureBottomSheet(
                        lecture = detailLecture,
                        onBottomSheetHeightChange = { bottomSheetHeight = it },
                        onClickLectureDelete = onClickBottomSheetDetailDelete,
                        onClickComplete = onClickBottomSheetDetailComplete
                    )
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetElevation = 20.dp
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .background(Color.White)
                .dynamicPadding(sheetState, bottomSheetHeight),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimetableScheduleBox(
                    currentSemester = currentSemester,
                    timetableName = timetableName,
                    onClick = onClickTimetableSchedule
                )
                TimetableDownloadBox(onClick = onClickDownloadTimetable)
            }
            Timetable(
                range = range,
                graphicsLayer = graphicsLayer,
                modifier = Modifier,
                events = timetableEvents,
                clickEvent = clickedTimetableEvents,
                etcClickEvent = etcClickedTimetableEvents,
                onEventClick = onClickTimetableEvent
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.dynamicPadding(
    sheetState: BottomSheetState,
    sheetHeight: Float
) = padding(
    bottom =
    if (sheetState.isExpanded) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimetableScreenPreview() {
    TimetableScreen(
        range = 9,
        customContents = CustomContentState(),
        lectures = listOf(dummyLecture),
        detailLecture = dummyLecture.toTimetableLecture(),
        semesters = listOf("20242"),
        timetableEvents = listOf(dummyEvent, dummyEvent.copy(dayOfWeek = DayOfWeek.MONDAY)),
        clickedTimetableEvents = listOf(dummyEvent, dummyEvent.copy(dayOfWeek = DayOfWeek.THURSDAY)),
        currentSemester = "20242",
        timetableName = "시간표1",
        selectedLecture = null,
        searchText = "",
        bottomSheetContentMode = TimetableBottomSheetContentMode.BASIC,
        bottomSheetUI = BottomSheetUI.DEFAULT,
        sheetState =
        rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        ),
        sheetLazyListState = rememberLazyListState(),
        scaffoldState = rememberBottomSheetScaffoldState()
    )
}
