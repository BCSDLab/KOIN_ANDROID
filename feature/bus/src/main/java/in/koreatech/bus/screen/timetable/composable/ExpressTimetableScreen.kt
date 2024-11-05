package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.screen.timetable.type.DaytimeType
import `in`.koreatech.bus.screen.timetable.type.ExpressDirectionType
import `in`.koreatech.bus.viewstate.ArrivalViewState
import `in`.koreatech.bus.viewstate.ExpressTimetableViewState
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipGroup
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
internal fun ExpressTimetableScreen(
    timetable: ExpressTimetableViewState,
    modifier: Modifier = Modifier,
    onDirectionChanged: (ExpressDirectionType) -> Unit = {}
) {

    var selectedDirectionTypeIndex by rememberSaveable { mutableIntStateOf(ExpressDirectionType.TO_BYEONGCHEON.ordinal) }
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        TextChipGroup(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 24.dp),
            titles = ExpressDirectionType.entries.map { stringResource(it.titleRes) },
            onChipSelected = { title ->
                selectedDirectionTypeIndex = ExpressDirectionType.entries.find { context.getString(it.titleRes) == title }?.ordinal ?: 0
            },
            selectedChipIndexes = intArrayOf(selectedDirectionTypeIndex)
        )

        CommonTimetableView(
            timetable = timetable,
            modifier = Modifier.fillMaxSize()
        )
    }

    LaunchedEffect(selectedDirectionTypeIndex) {
        onDirectionChanged(ExpressDirectionType.entries[selectedDirectionTypeIndex])
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpressTimetableScreenPreview() {
    ExpressTimetableScreen(
        modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
        timetable = ExpressTimetableViewState(
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
                    ArrivalViewState(
                        arrivalTime = "23:00"
                    ),
                    ArrivalViewState(
                        arrivalTime = "23:30"
                    )
                ),
            )
        )
    )
}