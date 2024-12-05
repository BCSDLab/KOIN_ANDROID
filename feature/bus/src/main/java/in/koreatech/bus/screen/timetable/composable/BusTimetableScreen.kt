package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.timetable.viewmodel.BusTimetableViewModel

@Composable
internal fun BusTimetableScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onNavigateToShuttleTimetableDetailScreen: (route: String) -> Unit = {},
    viewModel: BusTimetableViewModel = hiltViewModel(),
) {

    val busTimetableUiState by viewModel.timetableUiState.collectAsStateWithLifecycle()
    val busNoticeUiState by viewModel.noticeUiState.collectAsStateWithLifecycle()

    BusTimetableScreenContent(
        modifier = modifier,
        busTimetableUiState = busTimetableUiState,
        busNoticeUiState = busNoticeUiState,
        onNavigationIconClick = onNavigationIconClick,
        onNavigateToShuttleTimetableDetailScreen = onNavigateToShuttleTimetableDetailScreen,
        onCloseNotice = viewModel::closeNotice
    )
}
