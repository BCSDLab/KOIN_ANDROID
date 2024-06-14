package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.ui.timetablev2.component.DayHeader

@Composable
fun TimetableHeader(
    dayStartPadding: Dp,
    modifier: Modifier = Modifier,
    dayHeader: @Composable (day: String) -> Unit = { DayHeader(day = it) },
) {
    Row(
        modifier = modifier
            .background(Color.LightGray)
            .padding(start = dayStartPadding)
    ) {
        val days = listOf("월", "화", "수", "목", "금")
        repeat(days.size) {
            Box(modifier = Modifier.weight(1f)) {
                dayHeader(day = days[it])
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableHeaderPreview() {
    TimetableHeader(
        dayStartPadding = 10.dp
    )
}