package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
    onComplete: () -> Unit = {},
    onAddLecture: () -> Unit = {},
    onAddCustomLecture: () -> Unit = {},
    onSearchTextChange: (text: String) -> Unit = {},
    onSelectedLecture: (lecture: Lecture?) -> Unit = {},
    onBottomSheetHeightChange: (height: Float) -> Unit = {},
    onClickLecture: (events: List<TimetableEvent>) -> Unit = {},
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
            onAddLecture = onAddLecture,
            onAddCustomLecture = onAddCustomLecture
        )
        HorizontalDivider(thickness = 1.dp, color = KoinTheme.colors.neutral300)
        TimetableSearchBox(
            modifier = Modifier.padding(vertical = 8.dp),
            searchText = searchText,
            onSearchTextChange = onSearchTextChange
        )
        HorizontalDivider(thickness = 2.dp, color = KoinTheme.colors.neutral300)
        LazyColumn {
            itemsIndexed(lectures) { _, lecture ->
                LectureBox(
                    colors = colors,
                    lecture = lecture,
                    selectedLecture = selectedLecture,
                    onClickLecture = onClickLecture,
                    onSelectedLecture = onSelectedLecture,
                    onAddLecture = onAddLecture
                )
            }
        }
    }
}

@Preview
@Composable
private fun TimetableBottomSheetPreview() {
    KoinTheme {
        TimetableBottomSheet(
            searchText = "",
            lectures = listOf(dummyLecture, dummyLecture.copy(name = "컴퓨터 개발")),
            selectedLecture = dummyLecture,
        )
    }
}