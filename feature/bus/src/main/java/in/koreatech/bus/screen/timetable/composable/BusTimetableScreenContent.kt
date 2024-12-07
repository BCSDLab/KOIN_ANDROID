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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.busNoticeUiStateMock
import `in`.koreatech.bus.component.NoticeItem
import `in`.koreatech.bus.mock.shuttleCoursesMock
import `in`.koreatech.bus.screen.CommonLoadingView
import `in`.koreatech.bus.screen.timetable.type.BusType
import `in`.koreatech.bus.screen.timetable.type.DaytimeType
import `in`.koreatech.bus.screen.timetable.viewmodel.BusNoticeUiState
import `in`.koreatech.bus.screen.timetable.viewmodel.BusTimetableUiState
import `in`.koreatech.bus.viewstate.ArrivalViewState
import `in`.koreatech.bus.viewstate.BusNoticeViewState
import `in`.koreatech.bus.viewstate.CommonTimetableViewState
import `in`.koreatech.bus.viewstate.ShuttleCourseRouteState
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun BusTimetableScreenContent(
    busTimetableUiState: BusTimetableUiState,
    busNoticeUiState: BusNoticeUiState,
    modifier: Modifier = Modifier,
    onShuttleCourseRouteClick: (ShuttleCourseRouteState) -> Unit = {},
    onNavigationIconClick: () -> Unit = {},
    onCloseNotice: () -> Unit = {},
    onNoticeClick: (BusNoticeViewState) -> Unit = {},
    previewTab: BusType = BusType.SHUTTLE
) {

    var selectedTimetableTypeTab by rememberSaveable { mutableStateOf(BusType.SHUTTLE) }
    val busTypeHeadTitle = when(selectedTimetableTypeTab) {
        BusType.SHUTTLE -> stringResource(R.string.shuttle_timetable)
        BusType.EXPRESS -> stringResource(R.string.express_timetable)
        BusType.CITY -> stringResource(R.string.city_timetable)
    }

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { BusType.entries.size }

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp)
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
                    if (busNoticeUiState is BusNoticeUiState.Show) {
                        NoticeItem(
                            notice = busNoticeUiState.notice,
                            onCloseIconClick = onCloseNotice,
                            onNoticeClick = onNoticeClick
                        )
                    }
                }
            }

            stickyHeader {
                KoinTabRow(
                    titles = BusType.entries.map { stringResource(it.titleRes) },
                    selectedTabIndex = pagerState.currentPage,
                    onTabSelected = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    }
                )
            }

            item {
                if (LocalInspectionMode.current)
                    selectedTimetableTypeTab = previewTab

                when (busTimetableUiState) {
                    is BusTimetableUiState.Success -> {
                        HorizontalPager(
                            modifier = Modifier.fillMaxSize().background(Color.White),
                            state = pagerState,
                            verticalAlignment = Alignment.Top
                        ) { page ->
                            when (page) {
                                BusType.SHUTTLE.ordinal -> {
                                    ShuttleCoursesScreen(
                                        modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
                                        shuttleCourses = busTimetableUiState.shuttleCourses,
                                        onItemClicked = onShuttleCourseRouteClick
                                    )
                                }

                                BusType.EXPRESS.ordinal -> {
                                    ExpressTimetableScreen(
                                        modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
                                        timetable = busTimetableUiState.expressTimetable
                                    )
                                }

                                BusType.CITY.ordinal -> {
                                    CityTimetableContent(
                                        modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
                                        timetable = busTimetableUiState.cityTimetable
                                    )
                                }
                            }
                        }
                    }

                    is BusTimetableUiState.Loading -> {
                        CommonLoadingView(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 100.dp)
                        )
                    }

                    is BusTimetableUiState.LoadFailed -> {
                        // TODO 로드 실패, Pull To Refresh 있으면 좋을 듯.
                    }
                }
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTimetableTypeTab = BusType.entries[pagerState.currentPage]
    }
}

@Preview(showBackground = true)
@Composable
private fun BusTimetableShuttleScreenPreview() {
    BusTimetableScreenContent(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.SHUTTLE,
        busTimetableUiState = previewUiState,
        busNoticeUiState = busNoticeUiStateMock
    )
}
@Preview(showBackground = true)
@Composable
private fun BusTimetableExpressScreenPreview() {
    BusTimetableScreenContent(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.EXPRESS,
        busTimetableUiState = previewUiState,
        busNoticeUiState = busNoticeUiStateMock
    )
}
@Preview(showBackground = true)
@Composable
private fun BusTimetableCityScreenPreview() {
    BusTimetableScreenContent(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.CITY,
        busTimetableUiState = previewUiState,
        busNoticeUiState = busNoticeUiStateMock
    )
}
@Preview(showBackground = true)
@Composable
private fun BusTimetableLoadingScreenPreview() {
    BusTimetableScreenContent(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.CITY,
        busTimetableUiState = BusTimetableUiState.Loading,
        busNoticeUiState = busNoticeUiStateMock
    )
}

private val previewUiState = BusTimetableUiState.Success(
    shuttleCourses = shuttleCoursesMock,
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
