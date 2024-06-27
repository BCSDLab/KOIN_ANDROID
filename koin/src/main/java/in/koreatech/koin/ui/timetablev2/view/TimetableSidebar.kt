package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.ui.timetablev2.component.SidebarLabel
import java.time.LocalTime

@Composable
fun TimetableSidebar(
    hourHeight: Dp,
    hourWidth: Dp,
    modifier: Modifier = Modifier,
    label: @Composable (time: LocalTime) -> Unit = { SidebarLabel(time = it) },
) {
    val dividerColor = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray
    val startTime = LocalTime.of(9, 0)
    val times = 15

    Column(
        modifier = modifier,
    ) {
        repeat(times) {
            Box(
                modifier = Modifier
                    .size(height = hourHeight, width = hourWidth)
                    .drawBehind {
                        drawLine(
                            dividerColor,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
            ) {
                label(startTime.plusHours(it.toLong()))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableSidebarPreview() {
    TimetableSidebar(hourHeight = 64.dp, hourWidth = 68.dp)
}