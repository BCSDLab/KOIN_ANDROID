package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.screen.CommonLoadingView
import `in`.koreatech.bus.screen.timetable.type.BusType
import `in`.koreatech.bus.screen.timetable.type.DaytimeType
import `in`.koreatech.bus.screen.timetable.type.ShuttleBusRouteType
import `in`.koreatech.bus.screen.timetable.viewmodel.BusTimetableUiState
import `in`.koreatech.bus.viewstate.ArrivalViewState
import `in`.koreatech.bus.viewstate.CommonTimetableViewState
import `in`.koreatech.bus.viewstate.ShuttleRegionViewState
import `in`.koreatech.bus.viewstate.ShuttleTimetableOverviewViewState
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun BusTimetableScreenContent(
    busTimetableUiState: BusTimetableUiState,
    shouldShowNotice: Boolean,
    notice: String,
    modifier: Modifier = Modifier,
    onNavigateToShuttleTimetableDetailScreen: (route: String) -> Unit = {},
    onNavigationIconClick: () -> Unit = {},
    onCloseNotice: () -> Unit = {},
    previewTab: BusType = BusType.SHUTTLE
) {

    var selectedTimetableTypeTab by rememberSaveable { mutableStateOf(BusType.SHUTTLE) }
    val busTypeHeadTitle = when(selectedTimetableTypeTab) {
        BusType.SHUTTLE -> stringResource(R.string.shuttle_timetable)
        BusType.EXPRESS -> stringResource(R.string.express_timetable)
        BusType.CITY -> stringResource(R.string.city_timetable)
    }

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
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = busTypeHeadTitle,
                        style = KoinTheme.typography.bold20
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LeadingIconText(
                        text = stringResource(R.string.request_for_incorrect_information),
                        iconRes = R.drawable.ic_caution
                    )
                    if (shouldShowNotice) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .background(
                                    color = KoinTheme.colors.info100,
                                    shape = RoundedCornerShape(8.dp)
                                ).padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = notice,
                                style = KoinTheme.typography.medium14,
                                color = KoinTheme.colors.primary500,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(
                                modifier = Modifier.padding(start = 4.dp).size(16.dp).noRippleClickable {
                                    onCloseNotice()
                                },
                                imageVector = Icons.Rounded.Close,
                                contentDescription = notice,
                                tint = KoinTheme.colors.neutral300
                            )
                        }
                    }
                }
            }

            stickyHeader {
                KoinTabRow(
                    titles = BusType.entries.map { stringResource(it.titleRes) },
                    selectedTabIndex = selectedTimetableTypeTab.ordinal,
                    onTabSelected = { selectedTimetableTypeTab = BusType.entries[it] }
                )
            }

            item {
                if (LocalInspectionMode.current)
                    selectedTimetableTypeTab = previewTab

                when (busTimetableUiState) {
                    is BusTimetableUiState.Success -> when (selectedTimetableTypeTab) {
                        BusType.SHUTTLE -> {
                            ShuttleTimetableScreen(
                                modifier = Modifier.fillMaxSize()
                                    .background(KoinTheme.colors.neutral100),
                                regions = busTimetableUiState.shuttleRegions.toPersistentList(),
                                onItemClicked = onNavigateToShuttleTimetableDetailScreen
                            )
                        }

                        BusType.EXPRESS -> {
                            ExpressTimetableScreen(
                                modifier = Modifier.fillMaxSize()
                                    .background(KoinTheme.colors.neutral100),
                                timetable = busTimetableUiState.expressTimetable
                            )
                        }

                        BusType.CITY -> {
                            CityTimetableContent(
                                modifier = Modifier.fillMaxSize()
                                    .background(KoinTheme.colors.neutral100),
                                timetable = busTimetableUiState.cityTimetable
                            )
                        }
                    }
                    is BusTimetableUiState.Loading -> {
                        CommonLoadingView(modifier = Modifier.fillMaxSize().padding(top = 100.dp))
                    }
                    is BusTimetableUiState.LoadFailed -> {
                        // TODO 로드 실패, Pull To Refresh 있으면 좋을 듯.
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BusTimetableShuttleScreenPreview() {
    BusTimetableScreenContent(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.SHUTTLE,
        busTimetableUiState = previewUiState,
        shouldShowNotice = true,
        notice = "나랏말싸미 중국에 달라 어쩌구저쩌구 킹종대왕 갓종대왕"
    )
}
@Preview(showBackground = true)
@Composable
private fun BusTimetableExpressScreenPreview() {
    BusTimetableScreenContent(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.EXPRESS,
        busTimetableUiState = previewUiState,
        shouldShowNotice = true,
        notice = "나랏말싸미 중국에 달라 어쩌구저쩌구 킹종대왕 갓종대왕"
    )
}
@Preview(showBackground = true)
@Composable
private fun BusTimetableCityScreenPreview() {
    BusTimetableScreenContent(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.CITY,
        busTimetableUiState = previewUiState,
        shouldShowNotice = true,
        notice = "나랏말싸미 중국에 달라 어쩌구저쩌구 킹종대왕 갓종대왕"
    )
}
@Preview(showBackground = true)
@Composable
private fun BusTimetableLoadingScreenPreview() {
    BusTimetableScreenContent(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.CITY,
        busTimetableUiState = BusTimetableUiState.Loading,
        shouldShowNotice = true,
        notice = "나랏말싸미 중국에 달라 어쩌구저쩌구 킹종대왕 갓종대왕"
    )
}

private val previewUiState = BusTimetableUiState.Success(
    shuttleRegions = listOf(
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
                    description = "토요일, 일요일 운행"
                ),
                ShuttleTimetableOverviewViewState(
                    routeType = ShuttleBusRouteType.CIRCULATION,
                    name = "대전-서울",
                    description = "토요일, 천안아산역"
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
                    description = "금요일 하교 추가"
                )
            )
        )
    ),
    expressTimetable = CommonTimetableViewState(
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
    ),
    cityTimetable = CommonTimetableViewState(
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
