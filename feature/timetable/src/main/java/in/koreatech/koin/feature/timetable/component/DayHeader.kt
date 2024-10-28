package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.DayHeader(
    day: String,
    modifier: Modifier = Modifier,
    isEnd: Boolean = false
) {
    Box(
        modifier = modifier
            .weight(1f)
    ) {
        if (day.isNotEmpty()) {
            Text(
                text = day,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        if (!isEnd) {
                            drawLine(
                                color = Color.LightGray,
                                start = Offset(size.width, 0f),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1.dp.toPx(),
                            )
                        }
                    }
                    .padding(4.dp)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 60)
@Composable
private fun DayHeaderPreview() {
    Row(
        modifier = Modifier.padding(5.dp)
    ) {
        DayHeader(
            day = "화"
        )
        DayHeader(
            day = "화", isEnd = true
        )
    }
}