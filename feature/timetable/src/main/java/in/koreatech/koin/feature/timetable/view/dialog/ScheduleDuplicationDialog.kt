package `in`.koreatech.koin.feature.timetable.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.FilledTextButton
import `in`.koreatech.koin.feature.timetable.model.TimetableColor
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import java.time.DayOfWeek
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDuplicationDialog(
    timetableEvent: TimetableEvent,
    onConfirm: (Boolean) -> Unit,
    onDismiss: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = { onDismiss(false) },
        modifier = modifier
    ) {
        Surface(
            modifier =
            Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = KoinTheme.shapes.extraSmall,
            color = Color.White
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.schedule_duplication_title),
                    style = KoinTheme.typography.bold16,
                    color = KoinTheme.colors.neutral800
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text =
                    stringResource(
                        R.string.schedule_duplication_content,
                        timetableEvent.name,
                        calculateTimeContent(timetableEvent)
                    ),
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.neutral600,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                FilledTextButton(
                    modifier =
                    Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    text = "확인",
                    onClick = { onConfirm(false) }
                )
            }
        }
    }
}

private fun calculateTimeContent(timetableEvent: TimetableEvent): String {
    val startHour = "${timetableEvent.start.hour}"
    val endHour =
        if (timetableEvent.end == LocalTime.of(23, 59)) {
            "24"
        } else {
            "${timetableEvent.end.hour}"
        }
    val startMinutes =
        if (timetableEvent.start.minute == 0) {
            ""
        } else {
            ":${timetableEvent.start.minute}"
        }
    val endMinutes =
        if (timetableEvent.end.minute == 0) {
            ""
        } else {
            if (timetableEvent.end.minute == 59) {
                ""
            } else {
                ":${timetableEvent.end.minute}"
            }
        }
    return "(${timetableEvent.dayOfWeekToKorean()} ${startHour}$startMinutes-${endHour}$endMinutes)"
}

@Preview
@Composable
private fun ScheduleDuplicationDialogPreview() {
    ScheduleDuplicationDialog(
        timetableEvent =
        TimetableEvent(
            id = 0,
            lectureId = 0,
            name = "강의제목",
            color = TimetableColor(Color.White, Color.White),
            dayOfWeek = DayOfWeek.MONDAY,
            start = LocalTime.of(9, 30),
            end = LocalTime.of(23, 59),
            description = null
        ),
        onConfirm = {},
        onDismiss = {}
    )
}
