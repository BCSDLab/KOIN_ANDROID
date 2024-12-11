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
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.state.BusSearchResultState
import `in`.koreatech.bus.state.ImmutableLocalTime
import `in`.koreatech.bus.util.formatBeforeTime
import `in`.koreatech.bus.util.formatTime
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

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
            ReadOnlyTextChip(
                title = stringResource(result.busType.titleRes),   // TODO : 버스 종류
                containerColor = Color(0xFFFBEBD7),
                textStyle = KoinTheme.typography.regular12.copy(
                    color = KoinTheme.colors.neutral600
                )
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = result.departureTime.formatTime(), // TODO : 출발 시간
                style = KoinTheme.typography.bold20
            )
        }
        if (showBeforeTime)
            Text(
                text = result.departureTime.formatBeforeTime(currentTime.localTime), // TODO : 남은 시간
                style = KoinTheme.typography.bold16.copy(
                    color = KoinTheme.colors.info700
                )
            )
    }
}