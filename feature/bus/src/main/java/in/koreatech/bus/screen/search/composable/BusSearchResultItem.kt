package `in`.koreatech.bus.screen.search.composable

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
import `in`.koreatech.bus.viewstate.BusDepartureInfoViewState
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun BusSearchResultItem(
    info: BusDepartureInfoViewState,
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
                title = stringResource(info.type.titleRes),   // TODO : 버스 종류
                containerColor = Color(0xFFFBEBD7),
                textStyle = KoinTheme.typography.regular12.copy(
                    color = KoinTheme.colors.neutral600
                )
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = info.departureHour.toString() + ":" + info.departureMinute.toString().padStart(2, '0'), // TODO : 출발 시간
                style = KoinTheme.typography.bold20
            )
        }
        Text(
            text = "${info.remainingTime}분 전", // TODO : 남은 시간
            style = KoinTheme.typography.bold16.copy(
                color = KoinTheme.colors.info700
            )
        )
    }
}