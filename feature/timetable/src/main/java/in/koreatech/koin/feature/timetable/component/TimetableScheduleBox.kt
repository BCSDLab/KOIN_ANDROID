package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.timetable.R

@Composable
fun TimetableScheduleBox(
    currentSemester: String,
    timetableName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(Color.White)
            .border(
                width = 1.dp,
                color = RebrandKoinTheme.colors.neutral300,
                shape = RoundedCornerShape(10.dp)
            )
            .widthIn(max = (LocalConfiguration.current.screenWidthDp / 2).dp)
            .noRippleClickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 10.dp)
    ) {
        Text(
            text = if (currentSemester.isEmpty()) {
                stringResource(R.string.timetable_schedule_add_semester)
            } else {
                semesterTitle(
                    currentSemester = currentSemester,
                    timetableName = timetableName
                )
            },
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral800,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun semesterTitle(
    currentSemester: String,
    timetableName: String
): String {
    val (year, semester) = currentSemester.toYearAndSemester() ?: return ""
    val prefixContent = when {
        year.isEmpty() -> ""
        semester.isEmpty() -> stringResource(R.string.timetable_schedule_year, year)
        else -> stringResource(R.string.timetable_schedule_year_semester, year, semester)
    }
    return if (timetableName.isEmpty()) {
        prefixContent
    } else {
        "$prefixContent / $timetableName"
    }
}

private fun String.toYearAndSemester(): Pair<String, String>? {
    return try {
        val content = this.split("-")
        when (content.size) {
            1 -> {
                val year = content.getOrNull(0)?.substring(0, 4).orEmpty()
                val semester = content.getOrNull(0)?.substring(4).orEmpty()
                year to semester
            }

            2 -> content.getOrNull(0).orEmpty() to content.getOrNull(1).orEmpty()

            else -> "" to ""
        }
    } catch (e: Exception) {
        null
    }
}

@Preview
@Composable
private fun TimetableScheduleBoxPreview() {
    TimetableScheduleBox(
        currentSemester = "2024-여름",
        timetableName = "시간표1"
    )
}
