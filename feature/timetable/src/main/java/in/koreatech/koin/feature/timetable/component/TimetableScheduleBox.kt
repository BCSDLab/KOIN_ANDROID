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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun TimetableScheduleBox(
    currentSemester: String,
    timetableName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = KoinTheme.colors.neutral300,
                    shape = RoundedCornerShape(10.dp),
                )
                .widthIn(max = (LocalConfiguration.current.screenWidthDp / 2).dp)
                .noRippleClickable { onClick() }
                .padding(5.dp),
    ) {
        Text(
            text =
                if (currentSemester.isEmpty()) {
                    "학기 추가하기"
                } else {
                    currentSemester.toSemesterTitle(
                        timetableName,
                    )
                },
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral800,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun String.toSemesterTitle(timetableName: String): String {
    return try {
        val content = this.split("-")
        val prefixContent =
            when (content.size) {
                1 -> {
                    val year = content.getOrNull(0)?.substring(0, 4).orEmpty()
                    val semester = content.getOrNull(0)?.substring(4).orEmpty()
                    if (year.isEmpty()) {
                        ""
                    } else {
                        if (semester.isEmpty()) {
                            "${year}년"
                        } else {
                            "${year}년 ${semester}학기"
                        }
                    }
                }

                2 -> {
                    val year = content.getOrNull(0).orEmpty()
                    val semester = content.getOrNull(1).orEmpty()
                    if (year.isEmpty()) {
                        ""
                    } else {
                        if (semester.isEmpty()) {
                            "${year}년"
                        } else {
                            "${year}년 ${semester}학기"
                        }
                    }
                }

                else -> ""
            }
        if (timetableName.isEmpty()) {
            prefixContent
        } else {
            "$prefixContent / $timetableName"
        }
    } catch (e: Exception) {
        ""
    }
}

@Preview
@Composable
private fun TimetableScheduleBoxPreview() {
    TimetableScheduleBox(
        currentSemester = "2024-여름",
        timetableName = "시간표1",
    )
}
