package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.state.ArrivalViewState
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
internal fun CommonTimetableItem(
    arrival: ArrivalViewState,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = arrival.arrivalTime,
            style = textStyle,
        )
        HorizontalDivider(
            color = KoinTheme.colors.neutral200,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommonTimetableItemPreview() {
    CommonTimetableItem(
        arrival = ArrivalViewState(
            arrivalTime = "09:00"
        ),
        textStyle = KoinTheme.typography.bold18.copy(
            color = KoinTheme.colors.warning500
        ),
        modifier = Modifier.padding(vertical = 12.dp)
    )
}