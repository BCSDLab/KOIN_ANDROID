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
import java.time.LocalTime

@Composable
fun TimetableSidebar(
    hourWidth: Dp = 68.dp,
    hourHeight: Dp = 64.dp,
    modifier: Modifier = Modifier,
    startTime: LocalTime = LocalTime.of(9, 0),
    endStartTime: LocalTime = LocalTime.of(17, 0),
    label: @Composable (time: LocalTime) -> Unit = { SidebarLabel(time = it) },
) {
    val distanceTime = endStartTime.hour - startTime.hour
    Column(
        modifier = modifier,
    ) {
        repeat(distanceTime) { hour ->
            Box(
                modifier = Modifier
                    .size(height = hourHeight, width = hourWidth)
                    .drawBehind {
                        drawLine(
                            Color.LightGray,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )

                        if (distanceTime - 1 == hour) {
                            drawLine(
                                Color.LightGray,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }
            ) {
                label(startTime.plusHours(hour.toLong()))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true)
@Composable
private fun TimetableSidebarPreview() {
    TimetableSidebar()
}