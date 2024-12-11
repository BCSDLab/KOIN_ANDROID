package `in`.koreatech.bus.screen.searchresult.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.component.BusTypeChip
import `in`.koreatech.bus.mock.busSearchResultMock1
import `in`.koreatech.bus.state.BusSearchResultState
import `in`.koreatech.bus.state.ImmutableLocalTime
import `in`.koreatech.bus.util.formatBeforeTime
import `in`.koreatech.bus.util.formatTime
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import java.time.LocalTime

@Composable
fun BusSearchResultItem(
    result: BusSearchResultState,
    currentTime: ImmutableLocalTime,
    showBeforeTime: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            BusTypeChip(busType = result.busType)
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = result.departureTime.formatTime(),
                style = KoinTheme.typography.bold20
            )
        }
        if (showBeforeTime)
            Text(
                text = result.departureTime.formatBeforeTime(currentTime.localTime),
                style = KoinTheme.typography.bold16.copy(
                    color = KoinTheme.colors.info700
                )
            )
    }
}

@Preview(showBackground = true)
@Composable
private fun BusSearchResultItemPreview() {
    BusSearchResultItem(
        result = busSearchResultMock1,
        currentTime = ImmutableLocalTime(LocalTime.now()),
        showBeforeTime = true
    )
}