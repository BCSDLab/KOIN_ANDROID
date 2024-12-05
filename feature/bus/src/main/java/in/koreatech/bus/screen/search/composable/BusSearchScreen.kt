package `in`.koreatech.bus.screen.search.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.search.viewmodel.BusSearchViewModel

@Composable
fun BusSearchScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onSearch: (departure: String, arrival: String) -> Unit = { _, _ -> },
    viewModel: BusSearchViewModel = hiltViewModel()
) {

    val departure by viewModel.departure.collectAsStateWithLifecycle()
    val arrival by viewModel.arrival.collectAsStateWithLifecycle()
    val busNoticeUiState by viewModel.noticeUiState.collectAsStateWithLifecycle()

    BusSearchScreenContent(
        departure = departure,
        arrival = arrival,
        busNoticeUiState = busNoticeUiState,
        modifier = modifier,
        onNavigationIconClick = onNavigationIconClick,
        onSwapIconClicked = viewModel::swapDepartureAndArrival,
        onSearchClicked = { onSearch(departure, arrival) },
        onDepartureSet = viewModel::setDeparture,
        onArrivalSet = viewModel::setArrival,
        onCloseNotice = viewModel::closeNotice
    )
}
