package `in`.koreatech.bus.screen.search.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.search.viewmodel.BusSearchViewModel
import `in`.koreatech.bus.util.goToArticle

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

    val context = LocalContext.current

    BusSearchScreenContent(
        departure = departure,
        arrival = arrival,
        busNoticeUiState = busNoticeUiState,
        modifier = modifier,
        onNavigationIconClick = onNavigationIconClick,
        onSwapIconClick = viewModel::swapDepartureAndArrival,
        onSearchClick = { onSearch(departure, arrival) },
        onDepartureSet = viewModel::setDeparture,
        onArrivalSet = viewModel::setArrival,
        onCloseNotice = viewModel::closeNotice,
        onNoticeClick = { context.goToArticle(it.id) }
    )
}
