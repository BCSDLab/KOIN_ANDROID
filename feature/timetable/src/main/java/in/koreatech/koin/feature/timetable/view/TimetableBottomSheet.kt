package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.feature.timetable.component.LectureBox
import `in`.koreatech.koin.feature.timetable.component.TimetableSearchBox
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.defaultColors
import `in`.koreatech.koin.feature.timetable.model.dummyLecture
import `in`.koreatech.koin.feature.timetable.section.TimetableBottomSheetHeader

@Composable
fun TimetableBottomSheet(
    searchText: String,
    lectures: List<Lecture>,
    modifier: Modifier = Modifier,
    selectedLecture: Lecture? = null,
    colors: List<Color> = defaultColors,
    onClickAddCustomLectureMode: () -> Unit = {},
    onClickAddLectureMode: () -> Unit = {},
    onComplete: () -> Unit = {},
    onClickSettingIcon: () -> Unit = {},
    onClickSearchIcon: () -> Unit = {},
    onSearchTextChange: (text: String) -> Unit = {},
    onClickAddLecture: () -> Unit = {},
    onClickLecture: (events: List<TimetableEvent>) -> Unit = {},
    onSelectedLecture: (lecture: Lecture?) -> Unit = {},
    onBottomSheetHeightChange: (height: Float) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)
            .background(Color.White)
            .onGloballyPositioned {
                onBottomSheetHeightChange(it.size.height.toFloat())
            }
            .padding(
                horizontal = 24.dp,
                vertical = 10.dp
            ),
    ) {
        TimetableBottomSheetHeader(
            modifier = Modifier.padding(bottom = 4.dp),
            onComplete = onComplete,
            onClickAddLectureMode = onClickAddLectureMode,
            onClickAddCustomLectureMode = onClickAddCustomLectureMode
        )
        HorizontalDivider(thickness = 1.dp, color = KoinTheme.colors.neutral300)
        TimetableSearchBox(
            modifier = Modifier.padding(vertical = 8.dp),
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onClickSearchIcon = onClickSearchIcon,
            onClickSettingIcon = onClickSettingIcon
        )
        HorizontalDivider(thickness = 2.dp, color = KoinTheme.colors.neutral300)
        LazyColumn {
            items(lectures.size) {
                LectureBox(
                    position = it,
                    colors = colors,
                    lecture = lectures[it],
                    selectedLecture = selectedLecture,
                    onClickLecture = onClickLecture,
                    onSelectedLecture = onSelectedLecture,
                    onClickAddLecture = onClickAddLecture
                )
                HorizontalDivider(thickness = 1.dp, color = KoinTheme.colors.neutral300)
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
            selectedLecture = null,
        )
    }
}