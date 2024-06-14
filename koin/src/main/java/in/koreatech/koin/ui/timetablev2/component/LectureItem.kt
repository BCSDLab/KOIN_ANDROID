package `in`.koreatech.koin.ui.timetablev2.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onAddLecture: (Lecture) -> Unit,
    onClick: (List<TimetableEvent>) -> Unit,
) {
    val isSelected = selectedLecture == lecture

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (isSelected) {
                    Color.LightGray
                } else {
                    Color.White
                }
            )
            .selectable(
                selected = isSelected,
                onClick = {
                    onClick(lecture.toTimetableEvents(colors = colors))
                    if (isSelected) {
                        onSelect(Lecture())
                    } else {
                        onSelect(lecture)
                    }
                }
            )
            .padding(12.dp)
    ) {
        Text(
            text = lecture.name.toString(),
            style = MaterialTheme.typography.body1,
            color = Color.Black
        )
        Row {
            Text(text = lecture.professor.toString())
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = lecture.department.toString())
        }
        Row {
            Text(text = lecture.grades + "학점" + "/")
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = lecture.target + "/")
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = lecture.lectureClass + "분반" + "/")
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = lecture.regularNumber + "명" + "/")
        }
        if (isSelected) {
            Card(
                modifier = Modifier.background(Color.White),
                onClick = { onAddLecture(lecture) }
            ) {
                Text(text = "추가하기")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LectureItemPreview() {
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