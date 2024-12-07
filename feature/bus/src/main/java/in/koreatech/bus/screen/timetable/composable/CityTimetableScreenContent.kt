package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.type.CityBusNumberType
import `in`.koreatech.bus.type.CommonDirectionType
import `in`.koreatech.bus.type.DaytimeType
import `in`.koreatech.bus.state.ArrivalViewState
import `in`.koreatech.bus.state.CommonTimetableViewState
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipGroup
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@Composable
internal fun CityTimetableScreenContent(
    timetable: CommonTimetableViewState,
    modifier: Modifier = Modifier,
    onBusNumberChanged: (CityBusNumberType) -> Unit = {},
    onDirectionChanged: (CommonDirectionType) -> Unit = {}
) {

    var selectedBusNumberType by rememberSaveable { mutableStateOf(CityBusNumberType.N400) }
    var selectedDirectionType by rememberSaveable { mutableStateOf(CommonDirectionType.TO_BYEONGCHEON) }
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.routes),
                style = KoinTheme.typography.regular16,
                color = KoinTheme.colors.neutral600
            )
            TextChipGroup(
                modifier = Modifier.padding(start = 16.dp),
                titles = CityBusNumberType.entries.map { stringResource(it.titleRes) },
                onChipSelected = { title ->
                    selectedBusNumberType =
                        CityBusNumberType.entries.find { context.getString(it.titleRes) == title } ?: CityBusNumberType.N400
                },
                selectedChipIndexes = intArrayOf(selectedBusNumberType.ordinal)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 4.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.operating),
                style = KoinTheme.typography.regular16,
                color = KoinTheme.colors.neutral600
            )
            TextChipGroup(
                modifier = Modifier.padding(start = 16.dp),
                titles = CommonDirectionType.entries.map { stringResource(it.titleRes) },
                onChipSelected = { title ->
                    selectedDirectionType =
                        CommonDirectionType.entries.find { context.getString(it.titleRes) == title } ?: CommonDirectionType.TO_BYEONGCHEON
                },
                selectedChipIndexes = intArrayOf(selectedDirectionType.ordinal)
            )
        }

        CommonTimetableView(
            timetable = timetable,
            modifier = Modifier.fillMaxSize()
        )
    }

    LaunchedEffect(selectedBusNumberType) {
        onBusNumberChanged(selectedBusNumberType)
    }

    LaunchedEffect(selectedDirectionType) {
        onDirectionChanged(selectedDirectionType)
    }
}

@Preview
@Composable
private fun CityTimetableScreenPreview() {
    CityTimetableScreenContent(
        modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
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
                    )
                )
            )
        )
    )
}