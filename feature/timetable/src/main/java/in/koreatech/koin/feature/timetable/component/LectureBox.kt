package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.dummyLecture
import `in`.koreatech.koin.feature.timetable.utils.toTimetableEvents

@Composable
fun LectureBox(
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
            .padding(
                horizontal = 12.dp,
            )
            .background(
                color = if (isSelected) {
                    Color.Blue
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(4.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(
                text = lecture.name,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = lecture.professor,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Row {
                events.forEachIndexed { index, event ->
                    Text(
                        text = (if (index != 0) "/" else "") + event.dayOfWeekToKorean(),
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                events.forEachIndexed { index, event ->
                    Text(
                        text = (if (index != 0) "/" else "") + "${event.start}-${event.end}",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
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
        if (isSelected) {
            // select view
        }
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Blue,
                    shape = CircleShape
                )
        )
    }
}

@Preview
@Composable
private fun LectureBoxPreview() {
    LectureBox(
        colors = listOf(Color.Blue),
        lecture = dummyLecture,
        selectedLecture = null,
    )
}

@Preview
@Composable
private fun LectureBoxPreview_Selected() {
    LectureBox(
        colors = listOf(Color.Blue),
        lecture = dummyLecture,
        selectedLecture = dummyLecture,
    )
}