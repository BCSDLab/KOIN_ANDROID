package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.screen.timetable.type.BusType
import `in`.koreatech.bus.screen.timetable.type.ShuttleBusRouteType
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipGroup
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun BusTimetableScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {}
) {

    var selectedTimetableTypeTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title = "버스 시간표",
            onNavigationIconClick = onNavigationIconClick
        )

        LazyColumn {
            item {
                Column(
                    modifier = Modifier.padding(start = 24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.shuttle_timetable),
                        style = KoinTheme.typography.bold20
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LeadingIconText(
                        text = stringResource(R.string.request_for_incorrect_information),
                        iconRes = R.drawable.ic_caution
                    )
                }
            }

            stickyHeader {
                KoinTabRow(
                    titles = BusType.entries.map { stringResource(it.titleRes) },
                    selectedTabIndex = selectedTimetableTypeTabIndex,
                    onTabSelected = { selectedTimetableTypeTabIndex = it }
                )
            }

            item {
                when(selectedTimetableTypeTabIndex) {
                    BusType.SHUTTLE.ordinal -> {
                        ShuttleTimetableScreen(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun BusTimetableScreenPreview() {
    BusTimetableScreen(
        modifier = Modifier.fillMaxSize()
    )
}