package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.compose.ui.defaultColors
import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.ui.timetablev2.component.DepartmentCarouselCard
import `in`.koreatech.koin.ui.timetablev2.component.LectureItem
import `in`.koreatech.koin.ui.timetablev2.component.SearchBox

@Composable
fun TimetableBottomSheetContent(
    searchText: String,
    isKeyboardVisible: Boolean,
    colors: List<Color> = defaultColors,
    lectures: List<Lecture>,
    selectedLectures: Lecture,
    currentDepartments: List<Department>,
    modifier: Modifier = Modifier,
    onSetting: () -> Unit,
    onCancel: (Department) -> Unit,
    onAddLecture: () -> Unit,
    onSelectedLecture: (Lecture) -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onClickLecture: (List<TimetableEvent>) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(
                if (isKeyboardVisible) 500.dp else 350.dp
            ),
    ) {
        SearchBox(
            modifier = Modifier.padding(8.dp),
            searchText = searchText,
            onSearchTextChanged = onSearchTextChanged,
            onSetting = onSetting
        )
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            itemsIndexed(currentDepartments) { index, department ->
                DepartmentCarouselCard(
                    modifier = Modifier.padding(
                        end = if (index == currentDepartments.size) 0.dp else 4.dp
                    ),
                    department = department,
                    onCancel = onCancel
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn {
            itemsIndexed(lectures) { _, lecture ->
                LectureItem(
                    colors = colors,
                    lecture = lecture,
                    selectedLecture = selectedLectures,
                    onClick = onClickLecture,
                    onSelect = onSelectedLecture,
                    onAddLecture = onAddLecture
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableBottomSheetContentPreview() {
    TimetableBottomSheetContent(
        isKeyboardVisible = false,
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