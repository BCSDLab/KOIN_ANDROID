package `in`.koreatech.bus.screen.searchresult.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.searchresult.viewmodel.BusSearchResultViewModel
import `in`.koreatech.bus.util.LocalOnRefresh
import `in`.koreatech.bus.util.formatDateValue
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.toImmutableList

@Composable
fun BusSearchResultScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    viewModel: BusSearchResultViewModel = hiltViewModel()
) {
    val searchResultUiState by viewModel.searchResultUiState.collectAsStateWithLifecycle()
    val selectedDateIndex by viewModel.selectedDateIndex.collectAsStateWithLifecycle()
    val selectedDaytimeIndex by viewModel.selectedDaytimeIndex.collectAsStateWithLifecycle()
    val selectedHourIndex by viewModel.selectedHourIndex.collectAsStateWithLifecycle()
    val selectedMinuteIndex by viewModel.selectedMinuteIndex.collectAsStateWithLifecycle()
    val currentTime by viewModel.currentTime.collectAsStateWithLifecycle()
    val selectedBusMenu by viewModel.selectedBusTypeMenu.collectAsStateWithLifecycle()
    val resources = LocalContext.current.resources
    val dateList = remember(viewModel.localDates, resources) {
        viewModel.localDates.map { it.formatDateValue(resources) }.toImmutableList()
    }
    val amText = stringResource(R.string.am)
    val pmText = stringResource(R.string.pm)
    val daytimeList = remember(amText, pmText) { listOf(amText, pmText).toImmutableList() }

    CompositionLocalProvider(LocalOnRefresh provides viewModel::refresh) {
        BusSearchResultScreenContent(
            modifier = modifier,
            searchResultUiState = searchResultUiState,
            onNavigationIconClick = onNavigationIconClick,
            dateList = dateList,
            daytimeList = daytimeList,
            hourList = viewModel.hourList.toImmutableList(),
            minuteList = viewModel.minuteList.toImmutableList(),
            currentTime = currentTime,
            selectedDateIndex = selectedDateIndex,
            selectedDaytimeIndex = selectedDaytimeIndex,
            selectedHourIndex = selectedHourIndex,
            selectedMinuteIndex = selectedMinuteIndex,
            onMinDepartureTimeSetToNow = viewModel::setDepartureTimeToNow,
            onCompleteMinDepartureTime = viewModel::setDepartureTime,
            departure = viewModel.departure,
            arrival = viewModel.arrival,
            onBusTypeChange = viewModel::onBusTypeMenuChanged,
            selectedBusType = selectedBusMenu
        )
    }
}
