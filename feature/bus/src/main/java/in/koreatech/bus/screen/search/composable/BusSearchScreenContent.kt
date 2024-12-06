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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.component.NoticeItem
import `in`.koreatech.bus.screen.search.type.PlaceSelectMode
import `in`.koreatech.bus.screen.timetable.viewmodel.BusNoticeUiState
import `in`.koreatech.bus.viewstate.BusNoticeViewState
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.feature.bus.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusSearchScreenContent(
    departure: String,
    arrival: String,
    busNoticeUiState: BusNoticeUiState,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onSwapIconClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onDepartureSet: (String) -> Unit = {},
    onArrivalSet: (String) -> Unit = {},
    onCloseNotice: () -> Unit = {}
) {
    val context = LocalContext.current

    val searchButtonEnabled by remember(departure, arrival) { derivedStateOf { departure.isNotEmpty() && arrival.isNotEmpty() } }
    var placeSelectMode by remember { mutableStateOf(PlaceSelectMode.NONE) }

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
                onCloseIconClick = onCloseNotice,
                noticeMaxLines = 2
            )
        }

        BusSearchView(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            departure = departure,
            arrival = arrival,
            searchButtonEnabled = searchButtonEnabled,
            onSwapIconClicked = onSwapIconClicked,
            onSearchClicked = onSearchClicked,
            onDepartureFieldClicked = { placeSelectMode = PlaceSelectMode.DEPARTURE },
            onArrivalFieldClicked = { placeSelectMode = PlaceSelectMode.ARRIVAL }
        )
    }

    if (placeSelectMode != PlaceSelectMode.NONE) {
        SelectPlaceBottomSheet(
            onDismissRequest = { placeSelectMode = PlaceSelectMode.NONE },
            selectMode = placeSelectMode,
            onConfirmSelection = {
                if (placeSelectMode == PlaceSelectMode.DEPARTURE) {
                    placeSelectMode =
                        if (arrival.isEmpty()) PlaceSelectMode.ARRIVAL else PlaceSelectMode.NONE
                    onDepartureSet(context.getString(it.titleRes))
                }
                else if(placeSelectMode == PlaceSelectMode.ARRIVAL) {
                    placeSelectMode =
                        if (departure.isEmpty()) PlaceSelectMode.DEPARTURE else PlaceSelectMode.NONE
                    onArrivalSet(context.getString(it.titleRes))
                }
            },
            modifier = Modifier,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun BusSearchScreenPreview() {
    BusSearchScreenContent(
        departure = "",
        arrival = "",
        modifier = Modifier.fillMaxWidth(),
        busNoticeUiState = BusNoticeUiState.Show(
            BusNoticeViewState(
                id = 1,
                title = "[긴급] 9.27(금) 대학등교방향 천안셔틀버스 터미널 미정차 알림(천안역에서 승차바람)"
            )
        )

    )
}

@Preview(showBackground = true)
@Composable
private fun BusSearchScreen2Preview() {
    BusSearchScreenContent(
        departure = "코리아텍",
        arrival = "",
        modifier = Modifier.fillMaxWidth(),
        busNoticeUiState = BusNoticeUiState.Show(
            BusNoticeViewState(
                id = 1,
                title = "[긴급] 9.27(금) 대학등교방향 천안셔틀버스 터미널 미정차 알림(천안역에서 승차바람)"
            )
        )
    )
}
@Preview(showBackground = true)
@Composable
private fun BusSearchScreen3Preview() {
    BusSearchScreenContent(
        departure = "",
        arrival = "천안역",
        modifier = Modifier.fillMaxWidth(),
        busNoticeUiState = BusNoticeUiState.NotShow
    )
}

@Preview(showBackground = true)
@Composable
private fun BusSearchScreen4Preview() {
    BusSearchScreenContent(
        departure = "코리아텍",
        arrival = "천안역",
        modifier = Modifier.fillMaxWidth(),
        busNoticeUiState = BusNoticeUiState.Show(
            BusNoticeViewState(
                id = 1,
                title = "[긴급] 9.27(금) 대학등교방향 천안셔틀버스 터미널 미정차 알림(천안역에서 승차바람)"
            )
        )
    )
}