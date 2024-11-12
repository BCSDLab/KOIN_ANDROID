package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.model.SemesterType

@Composable
fun SemesterScreen(
    userTimetables: Map<SemesterModel, List<TimetableFrame>>,
    isAnonymous: Boolean,
    modifier: Modifier = Modifier,
    onClickTimetable: (TimetableFrame) -> Unit = {},
    onClickAddTimetable: () -> Unit = {},
    onClickEditTimetable: () -> Unit = {},
) {

}


@Preview
@Composable
private fun SemesterScreenPreview() {
    SemesterScreen(
        userTimetables = mutableMapOf(
            SemesterModel(2024, SemesterType.Fall) to listOf(
                TimetableFrame(0, "시간표1", true),
                TimetableFrame(1, "시간표2", false),
                TimetableFrame(2, "시간표3", false),
            ),
            SemesterModel(2024, SemesterType.Spring) to listOf(TimetableFrame(0, "시간표1", true)),
            SemesterModel(2024, SemesterType.Winter) to listOf(
                TimetableFrame(0, "시간표1", true),
                TimetableFrame(2, "시간표3", false)
            ),
        ),
        isAnonymous = false,
    )
}

@Preview
@Composable
private fun SemesterScreenAnonymousPreview() {
    SemesterScreen(
        userTimetables = mutableMapOf(
            SemesterModel(2024, SemesterType.Fall) to listOf(
                TimetableFrame(0, "시간표1", true),
            ),
            SemesterModel(2024, SemesterType.Spring) to listOf(TimetableFrame(0, "시간표1", true)),
            SemesterModel(2024, SemesterType.Winter) to listOf(
                TimetableFrame(0, "시간표1", true),
            ),
        ),
        isAnonymous = true,
    )
}