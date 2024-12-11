package `in`.koreatech.bus.screen.searchresult.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.searchresult.viewmodel.BusSearchResultViewModel
import `in`.koreatech.bus.util.LocalOnRefreshComposition
import `in`.koreatech.bus.util.formatDateValue
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

    CompositionLocalProvider(LocalOnRefreshComposition provides viewModel::refresh) {
        BusSearchResultScreenContent(
            modifier = modifier,
            searchResultUiState = searchResultUiState,
            onNavigationIconClick = onNavigationIconClick,
            dateList = viewModel.localDates.map { it.formatDateValue() }.toImmutableList(),
            daytimeList = viewModel.daytimeList.toImmutableList(),
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
        )
    }
}
