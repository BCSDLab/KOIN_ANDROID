package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.HighlightedText
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.model.SemesterType
import `in`.koreatech.koin.feature.timetable.viewmodel.ScreenState
import `in`.koreatech.koin.feature.timetable.viewmodel.ScreenStateUIMode

@Composable
fun SemesterScreen(
    state: ScreenState,
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
            .background(RebrandKoinTheme.colors.neutral0)
    ) {
        when (state.mode) {
            ScreenStateUIMode.BASIC -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 24.dp)
                ) {
                    if (isAnonymous) {
                        HighlightedText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .noRippleClickable { onClickLoginText() },
                            texts = stringArrayResource(id = R.array.semester_anonymous_login),
                            highlightIndices = listOf(0),
                            defaultStyle = RebrandKoinTheme.typography.medium14.copy(color = RebrandKoinTheme.colors.neutral500),
                            highlightStyle = RebrandKoinTheme.typography.bold14.copy(color = RebrandKoinTheme.colors.primary500),
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    LazyColumn {
                        userTimetables.entries.forEachIndexed { index, (semesterModel, timetableFrames) ->
                            semesterBlock(
                                semesterModel = semesterModel,
                                timetableFrames = timetableFrames,
                                isAnonymous = isAnonymous,
                                isFirstBlock = index == 0,
                                isLastBlock = index == userTimetables.size - 1,
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

            ScreenStateUIMode.EMPTY -> { // 유저의 학기가 없는 경우
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_semester_empty),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(id = R.string.semester_empty),
                        textAlign = TextAlign.Center,
                        style = RebrandKoinTheme.typography.regular14.copy(
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    )
                }
            }

            ScreenStateUIMode.IDLE -> {}
        }
    }
}

private fun LazyListScope.semesterBlock(
    semesterModel: SemesterModel,
    timetableFrames: List<TimetableFrame>,
    isAnonymous: Boolean,
    isFirstBlock: Boolean,
    isLastBlock: Boolean,
    onClickTimetable: (TimetableFrame) -> Unit = {},
    onClickAddTimetable: () -> Unit = {},
    onClickEditTimetable: (TimetableFrame) -> Unit = {}
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .semesterListItemBorder(
                    isFirst = isFirstBlock,
                    isLast = isLastBlock && timetableFrames.isEmpty()
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        id = R.string.semester_semester_format,
                        semesterModel.year,
                        stringResource(id = semesterModel.type.stringRes)
                    ),
                    style = RebrandKoinTheme.typography.bold15.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                )
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .noRippleClickable { onClickAddTimetable() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add_lecture),
                        tint = RebrandKoinTheme.colors.neutral500,
                        contentDescription = null
                    )
                }
            }
        }
    }

    timetableFrames.forEachIndexed { index, frame ->
        timetableFrameBlock(
            timetableFrame = frame,
            isAnonymous = isAnonymous,
            isLast = isLastBlock && index == timetableFrames.lastIndex,
            onClickTimetable = onClickTimetable,
            onClickEditTimetable = onClickEditTimetable
        )
    }
}

private fun LazyListScope.timetableFrameBlock(
    timetableFrame: TimetableFrame,
    isAnonymous: Boolean,
    isLast: Boolean,
    onClickTimetable: (TimetableFrame) -> Unit = {},
    onClickEditTimetable: (TimetableFrame) -> Unit
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .semesterListItemBorder(isFirst = false, isLast = isLast)
                .noRippleClickable { onClickTimetable(timetableFrame) }
                .padding(horizontal = 32.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f, false),
                    text = timetableFrame.timetableName,
                    style = RebrandKoinTheme.typography.medium15.copy(
                        RebrandKoinTheme.colors.neutral800
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                if (!isAnonymous && timetableFrame.isMain) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        modifier = Modifier
                            .noRippleClickable {
                                onClickEditTimetable(timetableFrame)
                            }
                            .size(24.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_timetable_bookmark),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.primary500
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }

            if (!isAnonymous) {
                Icon(
                    modifier = Modifier
                        .noRippleClickable {
                            onClickEditTimetable(timetableFrame)
                        }
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_timetable_settings),
                    contentDescription = stringResource(id = R.string.semester_edit_timetable_frame),
                    tint = RebrandKoinTheme.colors.neutral500
                )
            }
        }
    }
}

@Composable
private fun Modifier.semesterListItemBorder(
    isFirst: Boolean,
    isLast: Boolean,
    width: Dp = 1.dp,
    color: Color = RebrandKoinTheme.colors.neutral300,
    cornerRadius: Dp = 20.dp
): Modifier = drawBehind {
    val strokeWidth = width.toPx()
    val radius = cornerRadius.toPx()
    val top = if (isFirst) 0f else -radius
    val bottom = if (isLast) size.height else size.height + radius
    clipRect {
        drawRoundRect(
            color = color,
            topLeft = Offset(strokeWidth / 2, top + strokeWidth / 2),
            size = Size(size.width - strokeWidth, bottom - top - strokeWidth),
            cornerRadius = CornerRadius(radius),
            style = Stroke(width = strokeWidth)
        )
    }
}

@Preview
@Composable
private fun SemesterScreenPreview() {
    SemesterScreen(
        state = ScreenState(mode = ScreenStateUIMode.BASIC),
        userTimetables =
        mutableMapOf(
            SemesterModel(2024, SemesterType.Fall) to
                listOf(
                    TimetableFrame(0, "시간표3시간표3시간표3시간표3시간표3시간표3시간표3시간표3시간표3시간표3시간표3", true),
                    TimetableFrame(1, "시간표2", false),
                    TimetableFrame(2, "시간표3", false)
                ),
            SemesterModel(2024, SemesterType.Spring) to listOf(TimetableFrame(0, "시간표1", true)),
            SemesterModel(2024, SemesterType.Winter) to
                listOf(
                    TimetableFrame(0, "시간표1", true),
                    TimetableFrame(2, "시간표3시간표3시간표3시간표3시간표3시간표3시간표3시간표3시간표3시간표3시간표3", false)
                )
        ),
        isAnonymous = false
    )
}

@Preview
@Composable
private fun SemesterScreenAnonymousPreview() {
    SemesterScreen(
        state = ScreenState(mode = ScreenStateUIMode.BASIC),
        userTimetables =
        mutableMapOf(
            SemesterModel(2024, SemesterType.Fall) to
                listOf(
                    TimetableFrame(0, "시간표1", true)
                ),
            SemesterModel(2024, SemesterType.Spring) to listOf(TimetableFrame(0, "시간표1", true)),
            SemesterModel(2024, SemesterType.Winter) to
                listOf(
                    TimetableFrame(0, "시간표1", true)
                )
        ),
        isAnonymous = true
    )
}

@Preview
@Composable
private fun SemesterScreenEmptyPreview() {
    SemesterScreen(
        state = ScreenState(),
        userTimetables = mapOf(),
        isAnonymous = false
    )
}
