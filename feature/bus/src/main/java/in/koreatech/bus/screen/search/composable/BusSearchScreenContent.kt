package `in`.koreatech.bus.screen.search.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.component.NoticeItem
import `in`.koreatech.bus.mock.busNoticeUiStateMock
import `in`.koreatech.bus.screen.timetable.viewmodel.BusNoticeUiState
import `in`.koreatech.bus.state.BusNoticeState
import `in`.koreatech.bus.type.PlaceSelectMode
import `in`.koreatech.bus.type.PlaceType
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.feature.bus.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusSearchScreenContent(
    departure: PlaceType?,
    arrival: PlaceType?,
    busNoticeUiState: BusNoticeUiState,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onSwapIconClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onDepartureSet: (PlaceType) -> Unit = {},
    onArrivalSet: (PlaceType) -> Unit = {},
    onCloseNotice: () -> Unit = {},
    onNoticeClick: (BusNoticeState) -> Unit = {}
) {
    val context = LocalContext.current

    val searchButtonEnabled by remember(
        departure,
        arrival
    ) { derivedStateOf { departure != null && arrival != null } }
    var placeSelectMode by rememberSaveable { mutableStateOf(PlaceSelectMode.NONE) }

    val disabledArrival by remember(departure) {
        mutableStateOf(departure)
    }

    val disabledDeparture by remember(arrival) {
        mutableStateOf(arrival)
    }

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_search),
            onNavigationIconClick = onNavigationIconClick
        )

        if (busNoticeUiState is BusNoticeUiState.Show) {
            NoticeItem(
                modifier = Modifier.padding(horizontal = 24.dp),
                notice = busNoticeUiState.notice,
                onCloseIconClick = {
                    onCloseNotice()
                    EventLogger.logCampusClickEvent(
                        "bus_announcement_close",
                        "교통편 조회하기"
                    )
                },
                onNoticeClick = {
                    onNoticeClick(busNoticeUiState.notice)
                    EventLogger.logCampusClickEvent(
                        "bus_announcement",
                        "교통편 조회하기"
                    )
                },
                noticeMaxLines = 2
            )
        }

        BusSearchView(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            departure = departure?.titleRes?.let { stringResource(it) } ?: "",
            arrival = arrival?.titleRes?.let { stringResource(it) } ?: "",
            searchButtonEnabled = searchButtonEnabled,
            onSwapIconClicked = {
                EventLogger.logCampusClickEvent(
                    "swap_destination",
                    "스왑 버튼"
                )
                onSwapIconClick()
            },
            onSearchClicked = {
                EventLogger.logCampusClickEvent(
                    "search_bus",
                    "조회하기"
                )
                onSearchClick()
            },
            onDepartureFieldClicked = {
                EventLogger.logCampusClickEvent(
                    "departure_box",
                    "출발지 선택"
                )
                placeSelectMode = PlaceSelectMode.DEPARTURE
            },
            onArrivalFieldClicked = {
                EventLogger.logCampusClickEvent(
                    "arrival_box",
                    "목적지 선택"
                )
                placeSelectMode = PlaceSelectMode.ARRIVAL
            }
        )
    }

    if (placeSelectMode != PlaceSelectMode.NONE) {
        SelectPlaceBottomSheet(
            onDismissRequest = { placeSelectMode = PlaceSelectMode.NONE },
            selectMode = placeSelectMode,
            onConfirmSelection = {
                if (placeSelectMode == PlaceSelectMode.DEPARTURE) {
                    EventLogger.logCampusClickEvent(
                        "departure_location_confirm",
                        context.getString(it.titleRes)
                    )
                    placeSelectMode =
                        if (arrival == null) PlaceSelectMode.ARRIVAL else PlaceSelectMode.NONE
                    onDepartureSet(it)
                } else if (placeSelectMode == PlaceSelectMode.ARRIVAL) {
                    EventLogger.logCampusClickEvent(
                        "arrival_location_confirm",
                        context.getString(it.titleRes)
                    )
                    placeSelectMode =
                        if (departure == null) PlaceSelectMode.DEPARTURE else PlaceSelectMode.NONE
                    onArrivalSet(it)
                }
            },
            modifier = Modifier,
            disabledPlace = if (placeSelectMode == PlaceSelectMode.DEPARTURE) disabledDeparture else disabledArrival
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BusSearchScreenPreview() {
    BusSearchScreenContent(
        departure = null,
        arrival = null,
        modifier = Modifier.fillMaxWidth(),
        busNoticeUiState = busNoticeUiStateMock
    )
}

@Preview(showBackground = true)
@Composable
private fun BusSearchScreen2Preview() {
    BusSearchScreenContent(
        departure = PlaceType.KOREATECH,
        arrival = null,
        modifier = Modifier.fillMaxWidth(),
        busNoticeUiState = busNoticeUiStateMock
    )
}

@Preview(showBackground = true)
@Composable
private fun BusSearchScreen3Preview() {
    BusSearchScreenContent(
        departure = null,
        arrival = PlaceType.STATION,
        modifier = Modifier.fillMaxWidth(),
        busNoticeUiState = BusNoticeUiState.NotShow
    )
}

@Preview(showBackground = true)
@Composable
private fun BusSearchScreen4Preview() {
    BusSearchScreenContent(
        departure = PlaceType.KOREATECH,
        arrival = PlaceType.STATION,
        modifier = Modifier.fillMaxWidth(),
        busNoticeUiState = busNoticeUiStateMock
    )
}
