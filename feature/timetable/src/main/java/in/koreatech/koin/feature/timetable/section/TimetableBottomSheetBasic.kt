package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.feature.timetable.component.LectureBox
import `in`.koreatech.koin.feature.timetable.component.TimetableSearchBox
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyLecture

@Composable
fun TimetableBottomSheetBasic(
    searchText: String,
    lectures: List<Lecture>,
    modifier: Modifier = Modifier,
    selectedLecture: Lecture? = null,
    timetableEvents: List<TimetableEvent> = emptyList(),
    sheetLazyListState: LazyListState,
    onClickSettingIcon: (visible: Boolean) -> Unit = {},
    onClickSearchIcon: () -> Unit = {},
    onSearchTextChange: (text: String) -> Unit = {},
    onClickAddLecture: (lecture: Lecture) -> Unit = {},
    onClickRemoveLecture: (lecture: Lecture) -> Unit = {},
    onClickLecture: (events: List<TimetableEvent>) -> Unit = {},
    onSelectedLecture: (lecture: Lecture?) -> Unit = {},
) {
    val nestedScroll = rememberNestedScrollInteropConnection()
    Column(
        modifier =
            modifier
                .background(Color.White),
    ) {
        TimetableSearchBox(
            modifier = Modifier.padding(bottom = 8.dp),
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onClickSearchIcon = onClickSearchIcon,
            onClickSettingIcon = onClickSettingIcon,
        )
        HorizontalDivider(thickness = 2.dp, color = KoinTheme.colors.neutral300)
        LazyColumn(
            modifier = Modifier.nestedScroll(nestedScroll),
            state = sheetLazyListState,
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            items(lectures.size) {
                LectureBox(
                    position = it,
                    lecture = lectures[it],
                    selectedLecture = selectedLecture,
                    timetableEvents = timetableEvents,
                    onClickLecture = onClickLecture,
                    onSelectedLecture = onSelectedLecture,
                    onClickAddLecture = onClickAddLecture,
                    onClickRemoveLecture = onClickRemoveLecture,
                )
                HorizontalDivider(thickness = 1.dp, color = KoinTheme.colors.neutral300)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableBottomSheetBasicPreview() {
    TimetableBottomSheetBasic(
        searchText = "",
        lectures = listOf(dummyLecture, dummyLecture.copy(id = 2, name = "컴퓨터 개발")),
        sheetLazyListState = rememberLazyListState(),
    )
}
