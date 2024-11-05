package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.screen.timetable.type.DaytimeType
import `in`.koreatech.bus.viewstate.ArrivalViewState
import `in`.koreatech.bus.viewstate.CommonTimetableViewState
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@Composable
internal fun CommonTimetableView(
    timetable: CommonTimetableViewState,
    modifier: Modifier = Modifier
) {
    KoinSurface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.am),
                        style = KoinTheme.typography.regular16,
                        color = KoinTheme.colors.neutral600
                    )

                    timetable.arrivals[DaytimeType.AM]?.forEach {
                        CommonTimetableItem(
                            arrival = it,
                            textStyle = KoinTheme.typography.bold18.copy(
                                color = KoinTheme.colors.warning500
                            ),
                            modifier = Modifier
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.pm),
                        style = KoinTheme.typography.regular16,
                        color = KoinTheme.colors.neutral600
                    )

                    timetable.arrivals[DaytimeType.PM]?.forEach {
                        CommonTimetableItem(
                            arrival = it,
                            textStyle = KoinTheme.typography.bold18.copy(
                                color = KoinTheme.colors.info700
                            ),
                            modifier = Modifier
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.updated_at) + " " + timetable.updatedAt,
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral500
            )
        }
    }
}

@Preview
@Composable
private fun CommonTimetableViewPreview() {
    CommonTimetableView(
        modifier = Modifier.fillMaxSize(),
        timetable = CommonTimetableViewState(
            updatedAt = "2024-09-21",
            arrivals = mapOf(
                DaytimeType.AM to listOf(
                    ArrivalViewState(
                        arrivalTime = "09:00"
                    ),
                    ArrivalViewState(
                        arrivalTime = "09:30"
                    ),
                    ArrivalViewState(
                        arrivalTime = "10:00"
                    ),
                    ArrivalViewState(
                        arrivalTime = "10:30"
                    ),
                ), DaytimeType.PM to listOf(
                    ArrivalViewState(
                        arrivalTime = "14:30"
                    ),
                    ArrivalViewState(
                        arrivalTime = "21:00"
                    ),
                    ArrivalViewState(
                        arrivalTime = "21:30"
                    ),
                    ArrivalViewState(
                        arrivalTime = "22:00"
                    ),
                    ArrivalViewState(
                        arrivalTime = "22:30"
                    ),
                )
            )
        )
    )
}
