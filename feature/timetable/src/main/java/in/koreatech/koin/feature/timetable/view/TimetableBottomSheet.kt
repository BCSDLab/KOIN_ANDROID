package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import java.time.DayOfWeek
import java.time.LocalTime

enum class TimetableBottomSheetContentMode {
    CUSTOM, BASIC
}

data class CustomEventData(
    val schedule: String = "",
    val professor: String? = "",
    val data: List<CustomEventExtraData> = emptyList()
)

data class CustomEventExtraData(
    val position: Int = 0,
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val startTime: LocalTime = LocalTime.of(9, 0),
    val endTime: LocalTime = LocalTime.of(10, 0),
    val place: String = ""
)

@Composable
fun TimetableBottomSheet(
    searchText: String,
    lectures: List<Lecture>,
    bottomSheetContentMode: TimetableBottomSheetContentMode,
    modifier: Modifier = Modifier,
    selectedLecture: Lecture? = null,
    timetableEvents: List<TimetableEvent> = emptyList(),
    onClickAddCustomLectureMode: () -> Unit = {},
    onClickAddLectureMode: () -> Unit = {},
    onComplete: () -> Unit = {},
    onClickSettingIcon: () -> Unit = {},
    onClickSearchIcon: () -> Unit = {},
    onSearchTextChange: (text: String) -> Unit = {},
    onClickAddLecture: (lecture: Lecture) -> Unit = {},
    onClickRemoveLecture: (lecture: Lecture) -> Unit = {},
    onClickLecture: (events: List<TimetableEvent>) -> Unit = {},
    onSelectedLecture: (lecture: Lecture?) -> Unit = {},
    onBottomSheetHeightChange: (height: Float) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
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
            mode = bottomSheetContentMode,
            onClickAddLectureMode = onClickAddLectureMode,
            onClickAddCustomLectureMode = onClickAddCustomLectureMode
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
                    onClickSettingIcon = onClickSettingIcon,
                    onClickSearchIcon = onClickSearchIcon,
                    onSearchTextChange = onSearchTextChange,
                    onClickAddLecture = onClickAddLecture,
                    onClickRemoveLecture = onClickRemoveLecture,
                    onClickLecture = onClickLecture,
                    onSelectedLecture = onSelectedLecture,
                )
            }

            TimetableBottomSheetContentMode.CUSTOM -> {
                TimetableBottomSheetCustom()
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
            bottomSheetContentMode = TimetableBottomSheetContentMode.BASIC,
            selectedLecture = null,
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
            bottomSheetContentMode = TimetableBottomSheetContentMode.CUSTOM,
            selectedLecture = null,
        )
    }
}