package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.bus.component.WrongInformationText
import `in`.koreatech.bus.mock.commonTimetableMock
import `in`.koreatech.bus.state.CommonTimetableState
import `in`.koreatech.bus.util.LocalSelectedTimetableTab
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@Composable
internal fun CommonTimetableView(
    updatedAt: String,
    timetable: CommonTimetableState,
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

                    timetable.amDepartures.fastForEach {
                        CommonTimetableItem(
                            arrival = it,
                            textStyle =
                            KoinTheme.typography.bold18.copy(
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

                    timetable.pmDepartures.fastForEach {
                        CommonTimetableItem(
                            arrival = it,
                            textStyle =
                            KoinTheme.typography.bold18.copy(
                                color = KoinTheme.colors.info700
                            ),
                            modifier = Modifier
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.updated_at, updatedAt),
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral500
            )

            WrongInformationText(
                modifier = Modifier.padding(top = 4.dp),
                loggingEventValue = LocalSelectedTimetableTab.current.getEventValue()
            )
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@Preview
@Composable
private fun CommonTimetableViewPreview() {
    CommonTimetableView(
        modifier = Modifier.fillMaxSize(),
        timetable = commonTimetableMock,
        updatedAt = "2024-11-11"
    )
}
