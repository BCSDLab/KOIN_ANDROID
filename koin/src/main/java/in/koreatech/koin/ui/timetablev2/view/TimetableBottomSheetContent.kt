package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.model.timetable.TimetableEvent

@Composable
fun TimetableBottomSheetContent(
    colors: List<Color>,
    lectures: List<Lecture>,
    selectedLectures: Lecture,
    currentDepartments: List<Department>,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClickLecture: (List<TimetableEvent>) -> Unit,
    onSelectedLecture: (Lecture) -> Unit,
    onAddLecture: (Lecture) -> Unit,
    onSetting: () -> Unit,
    onCancel: (Department) -> Unit,
) {

}

@Preview(showBackground = true)
@Composable
private fun TimetableBottomSheetContentPreview() {
    TimetableBottomSheetContent(
        colors = emptyList(),
        lectures = emptyList(),
        selectedLectures = Lecture(),
        currentDepartments = emptyList(),
        searchText = "",
        onSearchTextChanged = {},
        onClickLecture = {},
        onSelectedLecture = {},
        onAddLecture = {},
        onSetting = {},
        onCancel = {}
    )
}