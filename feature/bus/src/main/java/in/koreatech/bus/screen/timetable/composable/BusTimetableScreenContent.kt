package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.mock.busNoticeUiStateMock
import `in`.koreatech.bus.component.NoticeItem
import `in`.koreatech.bus.mock.cityTimetableMock
import `in`.koreatech.bus.mock.expressTimetableMock
import `in`.koreatech.bus.mock.shuttleCoursesMock
import `in`.koreatech.bus.component.CommonFailureView
import `in`.koreatech.bus.component.CommonLoadingView
import `in`.koreatech.bus.screen.timetable.viewmodel.BusNoticeUiState
import `in`.koreatech.bus.screen.timetable.viewmodel.BusTimetableUiState
import `in`.koreatech.bus.state.BusNoticeState
import `in`.koreatech.bus.state.ShuttleCourseRouteState
import `in`.koreatech.bus.type.BusType
import `in`.koreatech.bus.type.CityBusNumberType
import `in`.koreatech.bus.type.CommonDirectionType
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusTimetableScreenContent(
    busTimetableUiState: BusTimetableUiState,
    busNoticeUiState: BusNoticeUiState,
    modifier: Modifier = Modifier,
    onShuttleCourseRouteClick: (ShuttleCourseRouteState) -> Unit = {},
    onExpressDirectionChange: (CommonDirectionType) -> Unit = {},
    onCityBusNumberChange: (CityBusNumberType) -> Unit = {},
    onCityDirectionChange: (CommonDirectionType) -> Unit = {},
    onNavigationIconClick: () -> Unit = {},
    onCloseNotice: () -> Unit = {},
    onNoticeClick: (BusNoticeState) -> Unit = {},
    previewTab: BusType = BusType.SHUTTLE
) {

    var selectedTimetableTypeTab by rememberSaveable { mutableStateOf(BusType.SHUTTLE) }
    val busTypeHeadTitle = when(selectedTimetableTypeTab) {
        BusType.SHUTTLE -> stringResource(R.string.shuttle_timetable)
        BusType.EXPRESS -> stringResource(R.string.express_timetable)
        BusType.CITY -> stringResource(R.string.city_timetable)
        else -> ""
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { BusType.entriesExceptAll.size }

    var expressDirectionGuideText by rememberSaveable { mutableStateOf(
        context.getString(R.string.guide_koreatech_station)
    ) }
    var cityDirectionGuideText by rememberSaveable { mutableStateOf(
        context.getString(R.string.guide_koreatech_station)
    ) }

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_timetable),
            onNavigationIconClick = onNavigationIconClick
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp)
            ) {
                Row {
                    Text(
                        text = busTypeHeadTitle,
                        style = KoinTheme.typography.bold20
                    )
                    if (pagerState.currentPage + 1 != BusType.SHUTTLE.ordinal) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = when (pagerState.currentPage + 1) {
                                BusType.EXPRESS.ordinal -> expressDirectionGuideText
                                BusType.CITY.ordinal -> cityDirectionGuideText
                                else -> ""
                            },
                            style = KoinTheme.typography.regular13.copy(color = KoinTheme.colors.primary500)
                        )
                        Icon(
                            modifier = Modifier.padding(start = 4.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_bus_station),
                            tint = KoinTheme.colors.primary500,
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (busNoticeUiState is BusNoticeUiState.Show) {
                    NoticeItem(
                        notice = busNoticeUiState.notice,
                        onCloseIconClick = onCloseNotice,
                        onNoticeClick = onNoticeClick
                    )
                }
            }

            KoinTabRow(
                modifier = Modifier.padding(top = 8.dp),
                titles = BusType.entriesExceptAll.map { stringResource(it.titleRes) },
                selectedTabIndex = if (LocalInspectionMode.current)
                        previewTab.ordinal
                    else pagerState.currentPage,
                onTabSelected = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )

            if (LocalInspectionMode.current)
                selectedTimetableTypeTab = previewTab

            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { page ->
                when (busTimetableUiState) {
                    is BusTimetableUiState.Success -> {
                        when (page + 1) {   // BusType.ALL 때문에 +1
                            BusType.SHUTTLE.ordinal -> {
                                ShuttleCoursesScreenContent(
                                    modifier = Modifier
                                        .background(KoinTheme.colors.neutral100)
                                        .verticalScroll(rememberScrollState()),
                                    shuttleCourses = busTimetableUiState.shuttleCourses,
                                    onItemClicked = onShuttleCourseRouteClick,
                                )
                            }

                            BusType.EXPRESS.ordinal -> {
                                ExpressTimetableScreenContent(
                                    modifier = Modifier
                                        .background(KoinTheme.colors.neutral100)
                                        .verticalScroll(rememberScrollState()),
                                    expressTimetable = busTimetableUiState.expressTimetable,
                                    onDirectionChanged = {
                                        expressDirectionGuideText =
                                            if (it == CommonDirectionType.TO_CHEONAN) context.getString(
                                                R.string.guide_koreatech_station
                                            ) else context.getString(R.string.guide_cheonan_station)

                                        onExpressDirectionChange(it)
                                    }
                                )
                            }

                            BusType.CITY.ordinal -> {
                                CityTimetableScreenContent(
                                    modifier = Modifier
                                        .background(KoinTheme.colors.neutral100)
                                        .verticalScroll(rememberScrollState()),
                                    timetable = busTimetableUiState.cityTimetable,
                                    onBusNumberChanged = onCityBusNumberChange,
                                    onDirectionChanged = {
                                        cityDirectionGuideText =
                                            if (it == CommonDirectionType.TO_CHEONAN)
                                                context.getString(R.string.guide_koreatech_station)
                                            else context.getString(R.string.guide_cheonan_station)

                                        onCityDirectionChange(it)
                                    }
                                )
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
                        CommonFailureView(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(pagerState.targetPage) {
        selectedTimetableTypeTab = BusType.entriesExceptAll[pagerState.targetPage]
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
    expressTimetable = expressTimetableMock,
    cityTimetable = cityTimetableMock
)
