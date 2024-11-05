package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.bus.screen.timetable.type.BusType
import `in`.koreatech.bus.screen.timetable.type.DaytimeType
import `in`.koreatech.bus.screen.timetable.type.ShuttleBusRouteType
import `in`.koreatech.bus.screen.timetable.viewmodel.BusTimetableViewModel
import `in`.koreatech.bus.viewstate.ArrivalViewState
import `in`.koreatech.bus.viewstate.CommonTimetableViewState
import `in`.koreatech.bus.viewstate.ShuttleRegionViewState
import `in`.koreatech.bus.viewstate.ShuttleTimetableOverviewViewState
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipGroup
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun BusTimetableScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    viewModel: BusTimetableViewModel = hiltViewModel()
) {

    var selectedTimetableTypeTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_timetable),
            onNavigationIconClick = onNavigationIconClick
        )

        LazyColumn {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(start = 24.dp)
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
                            modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
                            regions = persistentListOf(
                                ShuttleRegionViewState(
                                    name = "서울",
                                    timetableOverviews = listOf(
                                        ShuttleTimetableOverviewViewState(
                                            routeType = ShuttleBusRouteType.WEEKDAY,
                                            name = "서울-대전",
                                        ),
                                        ShuttleTimetableOverviewViewState(
                                            routeType = ShuttleBusRouteType.WEEKEND,
                                            name = "서울-대전",
                                        ),
                                        ShuttleTimetableOverviewViewState(
                                            routeType = ShuttleBusRouteType.CIRCULATION,
                                            name = "서울-대전",
                                        )
                                    )
                                ),
                                ShuttleRegionViewState(
                                    name = "대전",
                                    timetableOverviews = listOf(
                                        ShuttleTimetableOverviewViewState(
                                            routeType = ShuttleBusRouteType.WEEKDAY,
                                            name = "대전-서울",
                                        ),
                                        ShuttleTimetableOverviewViewState(
                                            routeType = ShuttleBusRouteType.WEEKEND,
                                            name = "대전-서울",
                                            description = "대전에서 서울로 이동하는 노선입니다."
                                        ),
                                        ShuttleTimetableOverviewViewState(
                                            routeType = ShuttleBusRouteType.CIRCULATION,
                                            name = "대전-서울",
                                            description = "대전에서 서울로 이동하는 노선입니다."
                                        )
                                    )
                                ),
                                ShuttleRegionViewState(
                                    name = "대구",
                                    timetableOverviews = listOf(
                                        ShuttleTimetableOverviewViewState(
                                            routeType = ShuttleBusRouteType.WEEKDAY,
                                            name = "대구-서울",
                                        ),
                                        ShuttleTimetableOverviewViewState(
                                            routeType = ShuttleBusRouteType.WEEKDAY,
                                            name = "대구-서울",
                                        ),
                                        ShuttleTimetableOverviewViewState(
                                            routeType = ShuttleBusRouteType.WEEKEND,
                                            name = "대구-서울",
                                        )
                                    )
                                )
                            )
                        )
                    }
                    BusType.EXPRESS.ordinal -> {
                        ExpressTimetableScreen(
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
                    BusType.CITY.ordinal -> {
                        CityTimetableScreen(
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