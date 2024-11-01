package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.component.icon.StableIcon
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyLecture
import `in`.koreatech.koin.feature.timetable.utils.toTimetableEvents

@Composable
fun LectureBox(
    position: Int,
    colors: List<Color>,
    lecture: Lecture,
    selectedLecture: Lecture?,
    modifier: Modifier = Modifier,
    onAddLecture: () -> Unit = {},
    onSelectedLecture: (lecture: Lecture?) -> Unit = {},
    onClickLecture: (timetableEvents: List<TimetableEvent>) -> Unit = {},
) {
    val isSelected = selectedLecture == lecture
//    val isSelected by remember(lecture, selectedLecture) {
//        derivedStateOf { selectedLecture == lecture }
//    }
    val events = lecture.toTimetableEvents(colors = colors)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = {
                    onClickLecture(events)
                    if (isSelected) {
                        onSelectedLecture(null)
                    } else {
                        onSelectedLecture(lecture)
                    }
                }
            )
            .background(
                color = if (isSelected) {
                    Color(0xFFF2F6FA)
                } else {
                    Color.White
                },
            )
            .padding(top = if (position != 0) 8.dp else 0.dp)
            .padding(end = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(
                text = lecture.name,
                style = KoinTheme.typography.bold12,
                color = KoinTheme.colors.neutral800,
            )
            Text(
                text = lecture.professor,
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral800
            )

            Row {
                events.forEachIndexed { index, event ->
                    Text(
                        text = event.dayOfWeekToKorean(),
                        style = KoinTheme.typography.regular12,
                        color = KoinTheme.colors.neutral800
                    )
                    Text(
                        text = "${event.formatClassTimeCode().first} ~ ${event.formatClassTimeCode().second}",
                        style = KoinTheme.typography.regular12,
                        color = KoinTheme.colors.neutral800
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
            Text(
                text = lecture.formatDescription(),
                fontSize = 12.sp,
                color = Color.Black,
                lineHeight = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        IconButton(onClick = onAddLecture) {
            StableIcon(
                drawableResId = R.drawable.ic_plus,
                tint = KoinTheme.colors.primary500,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
private fun LectureBoxPreview() {
    KoinTheme {
        LectureBox(
            position = 1,
            colors = listOf(Color.Blue),
            lecture = dummyLecture.copy(
                classTime = listOf(310, 311, 312, 313, 410, 411, 412, 413)
            ),
            selectedLecture = null,
        )
    }
}

@Preview
@Composable
private fun LectureBoxPreview_Selected() {
    KoinTheme {
        LectureBox(
            position = 2,
            colors = listOf(Color.Blue),
            lecture = dummyLecture,
            selectedLecture = dummyLecture,
        )
    }
}