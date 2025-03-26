package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyLecture
import `in`.koreatech.koin.feature.timetable.section.TimetableBottomSheetBasic
import `in`.koreatech.koin.feature.timetable.section.TimetableBottomSheetCustom
import `in`.koreatech.koin.feature.timetable.section.TimetableBottomSheetHeader
import `in`.koreatech.koin.feature.timetable.state.CustomContentState
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState

enum class TimetableBottomSheetContentMode {
    CUSTOM,
    BASIC
}

@Composable
fun TimetableBottomSheet(
    searchText: String,
    lectures: List<Lecture>,
    customContents: CustomContentState,
    bottomSheetContentMode: TimetableBottomSheetContentMode,
    modifier: Modifier = Modifier,
    selectedLecture: Lecture? = null,
    timetableEvents: List<TimetableEvent> = emptyList(),
    sheetLazyListState: LazyListState,
    onClickAddCustomLectureMode: () -> Unit = {},
    onClickAddLectureMode: (mode: TimetableBottomSheetContentMode) -> Unit = {},
    onComplete: () -> Unit = {},
    onClickSettingIcon: (visible: Boolean) -> Unit = {},
    onClickSearchIcon: () -> Unit = {},
    onSearchTextChange: (text: String) -> Unit = {},
    onClickAddLecture: (lecture: Lecture) -> Unit = {},
    onClickRemoveLecture: (lecture: Lecture) -> Unit = {},
    onClickLecture: (events: List<TimetableEvent>) -> Unit = {},
    onSelectedLecture: (lecture: Lecture?) -> Unit = {},
    onBottomSheetHeightChange: (height: Float) -> Unit = {},
    onScheduleNameChange: (text: String) -> Unit = {},
    onProfessorNameChange: (text: String) -> Unit = {},
    onExtraPlaceNameChange: (id: Int, text: String) -> Unit = { _, _ -> },
    onDayOfWeekChange: (content: CustomExtraContentState) -> Unit = { },
    onClickStartTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickEndTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickAddCustomContent: () -> Unit = {},
    onClickRemoveCustomContent: (id: Int) -> Unit = {}
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color.White)
            .onGloballyPositioned {
                onBottomSheetHeightChange(it.size.height.toFloat())
            }
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 10.dp
            )
    ) {
        TimetableBottomSheetHeader(
            modifier = Modifier.padding(bottom = 10.dp),
            mode = bottomSheetContentMode,
            onClickAddLectureMode = onClickAddLectureMode,
            onClickAddCustomLectureMode = onClickAddCustomLectureMode,
            onComplete = onComplete
        )
        HorizontalDivider(thickness = 1.dp, color = KoinTheme.colors.neutral300)
        Spacer(modifier = Modifier.height(8.dp))

        when (bottomSheetContentMode) {
            TimetableBottomSheetContentMode.BASIC -> {
                TimetableBottomSheetBasic(
                    searchText = searchText,
                    lectures = lectures,
                    selectedLecture = selectedLecture,
                    timetableEvents = timetableEvents,
                    sheetLazyListState = sheetLazyListState,
                    onClickSettingIcon = onClickSettingIcon,
                    onClickSearchIcon = onClickSearchIcon,
                    onSearchTextChange = onSearchTextChange,
                    onClickAddLecture = onClickAddLecture,
                    onClickRemoveLecture = onClickRemoveLecture,
                    onClickLecture = onClickLecture,
                    onSelectedLecture = onSelectedLecture
                )
            }

            TimetableBottomSheetContentMode.CUSTOM -> {
                TimetableBottomSheetCustom(
                    customContents = customContents,
                    sheetLazyListState = sheetLazyListState,
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableBottomSheetPreview() {
    KoinTheme {
        TimetableBottomSheet(
            searchText = "",
            lectures = listOf(dummyLecture, dummyLecture.copy(id = 2, name = "컴퓨터 개발")),
            customContents = CustomContentState(),
            bottomSheetContentMode = TimetableBottomSheetContentMode.BASIC,
            selectedLecture = null,
            sheetLazyListState = rememberLazyListState()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableBottomSheetPreview_Custom() {
    KoinTheme {
        TimetableBottomSheet(
            searchText = "",
            lectures = listOf(dummyLecture, dummyLecture.copy(id = 2, name = "컴퓨터 개발")),
            customContents = CustomContentState(),
            bottomSheetContentMode = TimetableBottomSheetContentMode.CUSTOM,
            selectedLecture = null,
            sheetLazyListState = rememberLazyListState()
        )
    }
}
