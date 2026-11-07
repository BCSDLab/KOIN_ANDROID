package `in`.koreatech.bus.screen.search.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import `in`.koreatech.koin.core.analytics.BusAnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
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
        KoinTopAppBar2(
            title = {
                Text(
                    text = stringResource(R.string.title_bus_search),
                    style = RebrandKoinTheme.typography.medium18
                )
            },
            onNavigationIconClick = onNavigationIconClick
        )

        BusSearchView(
            modifier =
            Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp),
            departure = departure?.titleRes?.let { stringResource(it) } ?: "",
            arrival = arrival?.titleRes?.let { stringResource(it) } ?: "",
            onSwapIconClicked = {
                EventLogger.logClickEvent(
                    EventAction.DA1,
                    BusAnalyticsConstant.Label.SEARCH_SWAP,
                    BusAnalyticsConstant.Value.SWAP
                )
                onSwapIconClick()
            },
            onDepartureFieldClicked = {
                EventLogger.logClickEvent(
                    EventAction.DA1,
                    BusAnalyticsConstant.Label.SEARCH_DEPARTURE_OPEN,
                    BusAnalyticsConstant.Value.DEPARTURE_OPEN
                )
                placeSelectMode = PlaceSelectMode.DEPARTURE
            },
            onArrivalFieldClicked = {
                EventLogger.logClickEvent(
                    EventAction.DA1,
                    BusAnalyticsConstant.Label.SEARCH_ARRIVAL_OPEN,
                    BusAnalyticsConstant.Value.ARRIVAL_OPEN
                )
                placeSelectMode = PlaceSelectMode.ARRIVAL
            }
        )

        if (busNoticeUiState is BusNoticeUiState.Show) {
            Spacer(modifier = Modifier.height(38.dp))

            NoticeItem(
                modifier = Modifier.padding(horizontal = 24.dp),
                notice = busNoticeUiState.notice,
                onCloseIconClick = {
                    onCloseNotice()
                    EventLogger.logClickEvent(
                        EventAction.DA1,
                        BusAnalyticsConstant.Label.NOTICE_CLOSE,
                        BusAnalyticsConstant.Value.BUS_SEARCH
                    )
                },
                onNoticeClick = {
                    onNoticeClick(busNoticeUiState.notice)
                    EventLogger.logClickEvent(
                        EventAction.DA1,
                        BusAnalyticsConstant.Label.NOTICE_OPEN,
                        BusAnalyticsConstant.Value.BUS_SEARCH
                    )
                },
                noticeMaxLines = 2
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 30.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            onClick = {
                EventLogger.logClickEvent(
                    EventAction.DA1,
                    BusAnalyticsConstant.Label.SEARCH_SUBMIT,
                    BusAnalyticsConstant.Value.SEARCH
                )
                onSearchClick()
            },
            enabled = searchButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.primary500,
                contentColor = RebrandKoinTheme.colors.neutral0,
                disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                disabledContentColor = RebrandKoinTheme.colors.neutral0
            ),
            contentPadding = PaddingValues(vertical = 12.dp),
            shape = RebrandKoinTheme.shapes.extraSmall
        ) {
            Text(
                text = stringResource(R.string.action_search),
                style = RebrandKoinTheme.typography.medium15
            )
        }
    }

    if (placeSelectMode != PlaceSelectMode.NONE) {
        SelectPlaceBottomSheet(
            onDismissRequest = { placeSelectMode = PlaceSelectMode.NONE },
            selectMode = placeSelectMode,
            onConfirmSelection = {
                if (placeSelectMode == PlaceSelectMode.DEPARTURE) {
                    EventLogger.logCustomEvent(
                        action = EventAction.DA1.value,
                        category = BusAnalyticsConstant.Category.SELECT,
                        label = BusAnalyticsConstant.Label.SEARCH_DEPARTURE_CONFIRM,
                        value = context.getString(it.titleRes)
                    )
                    placeSelectMode =
                        if (arrival == null) PlaceSelectMode.ARRIVAL else PlaceSelectMode.NONE
                    onDepartureSet(it)
                } else if (placeSelectMode == PlaceSelectMode.ARRIVAL) {
                    EventLogger.logCustomEvent(
                        action = EventAction.DA1.value,
                        category = BusAnalyticsConstant.Category.SELECT,
                        label = BusAnalyticsConstant.Label.SEARCH_ARRIVAL_CONFIRM,
                        value = context.getString(it.titleRes)
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
