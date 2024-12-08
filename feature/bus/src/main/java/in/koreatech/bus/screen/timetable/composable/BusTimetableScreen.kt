package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.timetable.viewmodel.BusTimetableViewModel
import `in`.koreatech.bus.util.goToArticle
import `in`.koreatech.bus.state.ShuttleCourseRouteState

@Composable
internal fun BusTimetableScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onNavigateToShuttleTimetableDetailScreen: (ShuttleCourseRouteState) -> Unit = {},
    viewModel: BusTimetableViewModel = hiltViewModel(),
) {

    val busTimetableUiState by viewModel.timetableUiState.collectAsStateWithLifecycle()
    val busNoticeUiState by viewModel.noticeUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    BusTimetableScreenContent(
        modifier = modifier,
        busTimetableUiState = busTimetableUiState,
        busNoticeUiState = busNoticeUiState,
        onNavigationIconClick = onNavigationIconClick,
        onShuttleCourseRouteClick = onNavigateToShuttleTimetableDetailScreen,
        onExpressDirectionChange = viewModel::onExpressDirectionChanged,
        onCloseNotice = viewModel::closeNotice,
        onNoticeClick = { context.goToArticle(it.id) }
    )
}
