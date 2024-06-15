package `in`.koreatech.koin.ui.timetablev2.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.ui.timetablev2.component.CustomAlertDialog

@Composable
fun LectureAddDialog(
    context: Context,
    visible: Boolean,
    lecture: Lecture,
    duplication: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onAddLecture: (Lecture) -> Unit,
) {
    if (visible) {
        CustomAlertDialog(
            onDismissRequest = onDismissRequest,
            content = {
                Column(
                    modifier = modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${lecture.name}(${lecture.lectureClass})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = if (duplication) "기존 강의 대신\n새로운 강의를 추가하시겠습니까?" else "강의를 추가하시겠습니까?",
                        fontSize = 12.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = { onAddLecture(lecture) }
                    ) {
                        Text(text = "추가하기")
                    }
                }
            }
        )
    }
}