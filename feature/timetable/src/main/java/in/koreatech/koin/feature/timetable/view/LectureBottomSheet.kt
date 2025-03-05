package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.model.dummyLecture

@Composable
fun LectureBottomSheet(
    lecture: TimetableLecture?,
    modifier: Modifier = Modifier,
    onBottomSheetHeightChange: (Float) -> Unit = {},
    onClickLectureDelete: (TimetableLecture) -> Unit = {},
    onClickComplete: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .height(220.dp)
                .fillMaxWidth()
                .background(Color.White)
                .onGloballyPositioned {
                    onBottomSheetHeightChange(it.size.height.toFloat())
                }
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 10.dp,
                ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LectureBottomSheetHeader(
            onClickLectureDelete = {
                lecture?.let(onClickLectureDelete)
            },
            onClickComplete = onClickComplete,
        )
        HorizontalDivider(thickness = 1.dp, color = KoinTheme.colors.neutral300)
        lecture?.let {
            LectureBottomSheetContent(
                lecture = it,
            )
        }
    }
}

@Composable
fun LectureBottomSheetHeader(
    modifier: Modifier = Modifier,
    onClickLectureDelete: () -> Unit = {},
    onClickComplete: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.timetable_bottom_sheet_lecture_delete),
            style = KoinTheme.typography.bold18,
            color = KoinTheme.colors.danger700,
            modifier =
                Modifier.clickable {
                    onClickLectureDelete()
                },
        )
        Text(
            text = stringResource(id = R.string.timetable_bottom_sheet_lecture_detail),
            style = KoinTheme.typography.medium18,
            color = KoinTheme.colors.primary500,
            modifier = Modifier,
        )
        Text(
            text = stringResource(id = R.string.timetable_bottom_sheet_complete),
            style = KoinTheme.typography.medium18,
            color = KoinTheme.colors.neutral800,
            modifier =
                Modifier.clickable {
                    onClickComplete()
                },
        )
    }
}

@Composable
fun LectureBottomSheetContent(
    lecture: TimetableLecture,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = lecture.classTitle,
            style = KoinTheme.typography.medium18,
            color = KoinTheme.colors.neutral800,
        )
        Text(
            text = lecture.professor,
            style = KoinTheme.typography.regular15,
            color = KoinTheme.colors.neutral800,
        )
        Text(
            text = lecture.getDetailTime(),
            style = KoinTheme.typography.regular15,
            color = KoinTheme.colors.neutral800,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LectureBottomSheetPreview() {
    LectureBottomSheet(
        lecture = dummyLecture.toTimetableLecture(),
    )
}
