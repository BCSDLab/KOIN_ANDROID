package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
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
    onSheetDismiss: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(RebrandKoinTheme.colors.neutral0)
            .onGloballyPositioned {
                onBottomSheetHeightChange(it.size.height.toFloat())
            }
            .padding(vertical = 12.dp, horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LectureBottomSheetHeader(
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            onSheetDismiss = onSheetDismiss
        )

        lecture?.let {
            LectureBottomSheetContent(
                lecture = it
            )
        }

        LectureBottomSheetFooter(
            onClickLectureDelete = {
                lecture?.let(onClickLectureDelete)
            },
            onClickComplete = onClickComplete
        )
    }
}

@Composable
fun LectureBottomSheetHeader(
    modifier: Modifier = Modifier,
    onSheetDismiss: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.timetable_bottom_sheet_lecture_detail),
            style = RebrandKoinTheme.typography.bold18.copy(color = RebrandKoinTheme.colors.primary500, fontWeight = FontWeight.SemiBold)
        )
        Icon(
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable(onClick = onSheetDismiss),
            imageVector = ImageVector.vectorResource(R.drawable.ic_close_round),
            contentDescription = null,
            tint = RebrandKoinTheme.colors.neutral800
        )
    }
}

@Composable
fun LectureBottomSheetContent(
    lecture: TimetableLecture,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = lecture.classTitle,
            style = RebrandKoinTheme.typography.medium12.copy(fontWeight = FontWeight.SemiBold),
            color = RebrandKoinTheme.colors.neutral800
        )
        Text(
            text = lecture.professor,
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral800
        )
        Text(
            text = lecture.getDetailTime(),
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral800
        )
        Text(
            text = stringResource(R.string.timetable_bottom_sheet_lecture_more_info, lecture.department, lecture.grades, lecture.code).trim(),
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral500
        )
    }
}

@Composable
fun LectureBottomSheetFooter(
    modifier: Modifier = Modifier,
    onClickLectureDelete: () -> Unit = {},
    onClickComplete: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            modifier = Modifier.weight(0.3f),
            shape = RebrandKoinTheme.shapes.medium,
            border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral400),
            onClick = onClickLectureDelete,
            contentPadding = PaddingValues(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.neutral0,
                contentColor = RebrandKoinTheme.colors.neutral600
            )
        ) {
            Text(
                text = stringResource(R.string.timetable_bottom_sheet_lecture_delete),
                style = RebrandKoinTheme.typography.bold16.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        Button(
            modifier = Modifier.weight(0.7f),
            shape = RebrandKoinTheme.shapes.medium,
            onClick = onClickComplete,
            contentPadding = PaddingValues(vertical = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.primary500,
                contentColor = RebrandKoinTheme.colors.neutral0
            )
        ) {
            Text(
                text = stringResource(R.string.timetable_bottom_sheet_complete),
                style = RebrandKoinTheme.typography.bold16.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LectureBottomSheetPreview() {
    LectureBottomSheet(
        lecture = dummyLecture.toTimetableLecture()
    )
}
