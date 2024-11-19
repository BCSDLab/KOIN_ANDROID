package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.icon.StableIcon
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.HighlightedText
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.model.SemesterType

@Composable
fun SemesterScreen(
    userTimetables: Map<SemesterModel, List<TimetableFrame>>,
    isAnonymous: Boolean,
    modifier: Modifier = Modifier,
    onClickTimetable: (SemesterModel, TimetableFrame) -> Unit = { _, _ -> },
    onClickAddTimetable: (SemesterModel) -> Unit = {},
    onClickEditTimetable: (SemesterModel, TimetableFrame) -> Unit = { _, _ -> },
    onClickLoginText: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KoinTheme.colors.neutral0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 24.dp)
        ) {
            if (isAnonymous) {
                HighlightedText(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .noRippleClickable { onClickLoginText() },
                    texts = stringArrayResource(id = R.array.semester_anonymous_login),
                    highlightIndices = listOf(0),
                    defaultStyle = KoinTheme.typography.medium14.copy(color = KoinTheme.colors.neutral500),
                    highlightStyle = KoinTheme.typography.bold14.copy(color = KoinTheme.colors.info600)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            LazyColumn(
                modifier = Modifier
            ) {
                userTimetables.forEach { semesterModel, timetableFrames ->
                    SemesterBlock(
                        semesterModel = semesterModel,
                        timetableFrames = timetableFrames,
                        isAnonymous = isAnonymous,
                        onClickTimetable = { timetableFrame ->
                            onClickTimetable(semesterModel, timetableFrame)
                        },
                        onClickAddTimetable = {
                            onClickAddTimetable(semesterModel)
                        },
                        onClickEditTimetable = {
                            onClickEditTimetable(semesterModel, it)
                        }
                    )
                }
            }
        }
    }

}

private fun LazyListScope.SemesterBlock(
    semesterModel: SemesterModel,
    timetableFrames: List<TimetableFrame>,
    isAnonymous: Boolean,
    onClickTimetable: (TimetableFrame) -> Unit = {},
    onClickAddTimetable: () -> Unit = {},
    onClickEditTimetable: (TimetableFrame) -> Unit = {}
) {
    item {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                thickness = 2.dp,
                color = KoinTheme.colors.neutral300
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(
                        id = R.string.semester_semester_format,
                        semesterModel.year,
                        stringResource(id = semesterModel.type.stringRes)
                    ),
                    style = KoinTheme.typography.bold20.copy(
                        color = KoinTheme.colors.neutral800
                    )
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .noRippleClickable { onClickAddTimetable() }
                ) {
                    StableIcon(
                        modifier = Modifier.align(Alignment.Center),
                        drawableResId = R.drawable.ic_plus
                    )
                }
            }
        }
    }

    timetableFrames.forEach { frame ->
        TimetableFrameBlock(
            timetableFrame = frame,
            isAnonymous = isAnonymous,
            onClickTimetable = onClickTimetable,
            onClickEditTimetable = onClickEditTimetable
        )
    }
}

private fun LazyListScope.TimetableFrameBlock(
    timetableFrame: TimetableFrame,
    isAnonymous: Boolean,
    onClickTimetable: (TimetableFrame) -> Unit = {},
    onClickEditTimetable: (TimetableFrame) -> Unit
) {
    item {
        HorizontalDivider(
            thickness = 1.dp,
            color = KoinTheme.colors.neutral300
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .noRippleClickable { onClickTimetable(timetableFrame) }
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timetableFrame.timetableName,
                    style = KoinTheme.typography.medium18.copy(
                        KoinTheme.colors.neutral800
                    )
                )
                if (!isAnonymous && timetableFrame.isMain) {
                    StableIcon(
                        drawableResId = R.drawable.ic_flag_main,
                        tint = Color.Unspecified
                    )
                }
            }

            if (!isAnonymous) {
                Text(
                    modifier = Modifier.noRippleClickable {
                        onClickEditTimetable(timetableFrame)
                    },
                    text = stringResource(id = R.string.semester_edit_timetable_frame),
                    style = KoinTheme.typography.bold16
                )
            }
        }
    }
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