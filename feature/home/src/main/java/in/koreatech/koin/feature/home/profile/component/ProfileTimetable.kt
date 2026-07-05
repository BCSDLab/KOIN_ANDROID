package `in`.koreatech.koin.feature.home.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.home.profile.model.ProfileTimetableLecture
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ProfileTimetable(
    lectures: ImmutableList<ProfileTimetableLecture>,
    modifier: Modifier = Modifier
) {
    val startHour = ProfileTimetableDefaults.START_HOUR
    val endHour = ProfileTimetableDefaults.END_HOUR
    val rowCount = endHour - startHour
    val days = ProfileTimetableDefaults.days

    val timeCellPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
    val headerCellPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)

    Layout(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        content = {
            Box(
                modifier = Modifier
                    .layoutId("topLeft")
                    .drawBehind {
                        drawLine(ProfileTimetableDefaults.gridLineColor, Offset(size.width, 0f), Offset(size.width, size.height), 1f)
                        drawLine(ProfileTimetableDefaults.gridLineColor, Offset(0f, size.height), Offset(size.width, size.height), 1f)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = " ",
                    modifier = Modifier.padding(headerCellPadding),
                    style = RebrandKoinTheme.typography.regular12
                )
            }
            Column(
                modifier = Modifier
                    .layoutId("timeColumn")
                    .width(IntrinsicSize.Max)
                    .drawBehind {
                        drawLine(ProfileTimetableDefaults.gridLineColor, Offset(size.width, 0f), Offset(size.width, size.height), 1f)

                        val hHeight = size.height / (rowCount + 1)
                        for (i in 1..rowCount) {
                            val y = i * hHeight
                            drawLine(ProfileTimetableDefaults.gridLineColor, Offset(0f, y), Offset(size.width, y), 1f)
                        }
                    }
            ) {
                for (i in 0..rowCount) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(timeCellPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (startHour + i).toString(),
                            textAlign = TextAlign.Center,
                            color = RebrandKoinTheme.colors.neutral500,
                            style = RebrandKoinTheme.typography.regular10
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .layoutId("headerRow")
                    .drawBehind {
                        drawLine(ProfileTimetableDefaults.gridLineColor, Offset(0f, size.height), Offset(size.width, size.height), 1f)
                        val dWidth = size.width / days.size
                        for (d in 1 until days.size) {
                            val x = d * dWidth
                            drawLine(ProfileTimetableDefaults.gridLineColor, Offset(x, 0f), Offset(x, size.height), 1f)
                        }
                    }
            ) {
                days.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(headerCellPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            textAlign = TextAlign.Center,
                            color = RebrandKoinTheme.colors.neutral500,
                            style = RebrandKoinTheme.typography.regular12
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .layoutId("gridArea")
                    .drawBehind {
                        val totalHalfSlots = (rowCount + 1) * 2
                        val halfHeight = size.height / totalHalfSlots

                        for (i in 1 until totalHalfSlots) {
                            val y = i * halfHeight
                            drawLine(ProfileTimetableDefaults.gridLineColor, Offset(0f, y), Offset(size.width, y), 1f)
                        }

                        val dWidth = size.width / days.size
                        for (d in 1 until days.size) {
                            val x = d * dWidth
                            drawLine(ProfileTimetableDefaults.gridLineColor, Offset(x, 0f), Offset(x, size.height), 1f)
                        }
                    }
            )

            lectures.forEachIndexed { index, lecture ->
                if (lecture.dayOfWeek in 0..4 && lecture.startTotalMinutes < lecture.endTotalMinutes) {
                    val color = ProfileTimetableDefaults.colors[lecture.colorIndex % ProfileTimetableDefaults.colors.size]
                    Column(
                        modifier = Modifier
                            .layoutId("lecture_$index")
                            .background(color.content)
                    ) {
                        HorizontalDivider(color = color.header, thickness = 2.dp)
                        Column(modifier = Modifier.padding(4.dp)) {
                            Text(
                                text = lecture.name,
                                color = RebrandKoinTheme.colors.neutral800,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = RebrandKoinTheme.typography.medium13.copy(fontSize = 8.sp, lineHeight = 9.sp)
                            )
                            if (lecture.place.isNotBlank()) {
                                Text(
                                    text = lecture.place,
                                    modifier = Modifier.padding(top = 2.dp),
                                    color = RebrandKoinTheme.colors.neutral800,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = RebrandKoinTheme.typography.regular10.copy(fontSize = 7.sp, lineHeight = 8.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { measurables, constraints ->
        val topLeftMeasurable = measurables.first { it.layoutId == "topLeft" }
        val timeColumnMeasurable = measurables.first { it.layoutId == "timeColumn" }
        val headerRowMeasurable = measurables.first { it.layoutId == "headerRow" }
        val gridAreaMeasurable = measurables.first { it.layoutId == "gridArea" }

        val timeColPlaceable = timeColumnMeasurable.measure(constraints.copy(minWidth = 0))
        val timeColWidth = timeColPlaceable.width

        val headerMaxWidth = constraints.maxWidth - timeColWidth
        val headerRowPlaceable = headerRowMeasurable.measure(
            constraints.copy(minWidth = headerMaxWidth, maxWidth = headerMaxWidth)
        )
        val headerHeight = headerRowPlaceable.height

        val topLeftPlaceable = topLeftMeasurable.measure(
            constraints.copy(
                minWidth = timeColWidth,
                maxWidth = timeColWidth,
                minHeight = headerHeight,
                maxHeight = headerHeight
            )
        )

        val totalGridHeight = timeColPlaceable.height
        val rowCountInclusive = rowCount + 1
        val hourHeight = totalGridHeight.toFloat() / rowCountInclusive
        val dayWidth = headerMaxWidth.toFloat() / days.size

        val gridPlaceable = gridAreaMeasurable.measure(
            constraints.copy(
                minWidth = headerMaxWidth,
                maxWidth = headerMaxWidth,
                minHeight = totalGridHeight,
                maxHeight = totalGridHeight
            )
        )

        val lectureItems = measurables.mapNotNull { measurable ->
            val layoutId = measurable.layoutId as? String

            if (layoutId?.startsWith("lecture_") == true) {
                val index = layoutId.removePrefix("lecture_").toInt()
                val lecture = lectures[index]

                val durationMinutes = lecture.endTotalMinutes - lecture.startTotalMinutes
                val blockHeight = (hourHeight * (durationMinutes / 60f)).roundToInt()
                val blockWidth = dayWidth.roundToInt()

                val placeable = measurable.measure(
                    constraints.copy(
                        minWidth = blockWidth,
                        maxWidth = blockWidth,
                        minHeight = blockHeight,
                        maxHeight = blockHeight
                    )
                )
                Pair(placeable, lecture)
            } else {
                null
            }
        }

        layout(constraints.maxWidth, headerHeight + totalGridHeight) {
            topLeftPlaceable.placeRelative(0, 0)
            headerRowPlaceable.placeRelative(timeColWidth, 0)
            timeColPlaceable.placeRelative(0, headerHeight)
            gridPlaceable.placeRelative(timeColWidth, headerHeight)

            lectureItems.forEach { (placeable, lecture) ->
                val startMinutes = lecture.startTotalMinutes - (startHour * 60)
                val xOffset = timeColWidth + (dayWidth * lecture.dayOfWeek).roundToInt()
                val yOffset = headerHeight + (hourHeight * (startMinutes / 60f)).roundToInt()
                placeable.placeRelative(xOffset, yOffset)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileTimetableEmptyPreview() {
    ProfileTimetable(lectures = persistentListOf())
}

@Preview(showBackground = true)
@Composable
private fun ProfileTimetableFilledPreview() {
    ProfileTimetable(
        lectures = persistentListOf(
            ProfileTimetableLecture("직업능력개발훈련평가", "2공 105호", 0, 9 * 60, 10 * 60 + 30, 0),
            ProfileTimetableLecture("직업능력개발훈련평가", "2공 105호", 0, 11 * 60, 13 * 60, 6),
            ProfileTimetableLecture("직업능력개발훈련평가", "2공 105호", 0, 14 * 60, 16 * 60, 3),
            ProfileTimetableLecture("직업능력개발훈련평가", "2공 105호", 0, 16 * 60, 18 * 60, 12),
            ProfileTimetableLecture("직업능력개발훈련평가", "2공 105호", 1, 11 * 60, 13 * 60, 6),
            ProfileTimetableLecture("직업능력개발훈련평가", "2공 105호", 1, 16 * 60, 18 * 60, 6),
            ProfileTimetableLecture("직업능력개발훈련평가", "2공 105호", 2, 14 * 60, 16 * 60, 10),
            ProfileTimetableLecture("직업능력개발훈련평가", "2공 105호", 3, 14 * 60, 16 * 60, 12)
        )
    )
}
