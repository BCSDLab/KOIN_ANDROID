package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.component.DayHeader

@Composable
fun TimetableHeader(
    modifier: Modifier = Modifier,
    days: List<String> = listOf("월", "화", "수", "목", "금"),
) {
    Row(
        modifier = modifier
            .border(
                width = 0.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
            )
    ) {
        days.forEachIndexed { index, day ->
            DayHeader(day = day, isEnd = index == days.size - 1)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, heightDp = 30)
@Preview(showBackground = true, heightDp = 30)
@Composable
private fun TimetableHeaderPreview() {
    TimetableHeader(
        modifier = Modifier.padding(2.dp)
    )
}