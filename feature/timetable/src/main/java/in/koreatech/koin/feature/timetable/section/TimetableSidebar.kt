package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.component.SidebarLabel
import `in`.koreatech.koin.feature.timetable.model.TimetableConstants
import java.time.LocalTime

@Composable
fun TimetableSidebar(
    range: Int,
    hourWidth: Dp,
    modifier: Modifier = Modifier,
    hourHeight: Dp = (TimetableConstants.eventHeight).dp,
    initialTime: LocalTime = LocalTime.of(9, 0),
    label: @Composable (time: LocalTime) -> Unit = { SidebarLabel(time = it) },
) {
    Column(
        modifier = modifier,
    ) {
        repeat(range) { hour ->
            Box(
                modifier = Modifier
                    .size(height = hourHeight, width = hourWidth)
                    .drawBehind {
                        drawLine( // | (맨 앞)
                            color = Color.LightGray,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawLine( // ― (맨 위)
                            color = Color.LightGray,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )

                        if (range - 1 == hour) { // 마지막 ― (맨 아래)
                            drawLine(
                                color = Color.LightGray,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }
            ) {
                label(initialTime.plusHours(hour.toLong()))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true)
@Composable
private fun TimetableSidebarPreview() {
    TimetableSidebar(
        range = 9,
        hourWidth = 68.dp
    )
}