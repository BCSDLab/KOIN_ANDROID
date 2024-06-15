package `in`.koreatech.koin.ui.timetablev2.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.compose.ui.ColorMain400
import `in`.koreatech.koin.compose.ui.ColorPrimaryMain400_ALPAH10
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.util.ext.toTimetableEvents

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LectureItem(
    lecture: Lecture,
    colors: List<Color>,
    selectedLecture: Lecture,
    modifier: Modifier = Modifier,
    onSelect: (Lecture) -> Unit,
    onAddLecture: () -> Unit,
    onClick: (List<TimetableEvent>) -> Unit,
) {
    val isSelected = selectedLecture == lecture
    val events = lecture.toTimetableEvents(colors = colors)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = {
                    onClick(events)
                    if (isSelected) {
                        onSelect(Lecture())
                    } else {
                        onSelect(lecture)
                    }
                }
            )
            .padding(
                horizontal = 12.dp,
                vertical = 4.dp
            )
            .background(
                color = if (isSelected) {
                    ColorPrimaryMain400_ALPAH10
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(4.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = lecture.name,
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
                    text = (if (index != 0) "/" else "") + "${event.start} ~ ${event.end}",
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
        }
        Text(
            text = lecture.formatDescription(),
            fontSize = 12.sp,
            color = Color.Black
        )
        if (isSelected) {
            Card(
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, ColorMain400),
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .wrapContentSize(),
                onClick = onAddLecture
            ) {
                Text(
                    text = "추가",
                    color = ColorMain400,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 4.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LectureItemPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        LectureItem(
            colors = emptyList(),
            lecture = Lecture(
                name = "직업능력개발훈련평가",
                professor = "우성민",
                code = "HRD011",
                grades = "2",
                lectureClass = "01",
                regularNumber = "40",
                department = "HRD학과",
                target = "전기3",
                isEnglish = "",
                isElearning = "",
                designScore = "0",
                classTime = listOf(
                    310,
                    311,
                    312,
                    313
                )
            ),
            selectedLecture = Lecture(),
            onClick = {},
            onSelect = {},
            onAddLecture = {}
        )
    }
}