package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.timetable.viewmodel.BusTimetableViewModel
import `in`.koreatech.bus.state.ShuttleCourseRouteState
import `in`.koreatech.bus.util.LocalOnRefresh
import `in`.koreatech.bus.util.goToArticle

@Composable
internal fun BusTimetableScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onNavigateToShuttleTimetableScreen: (ShuttleCourseRouteState) -> Unit = {},
    viewModel: BusTimetableViewModel = hiltViewModel(),
) {
    val busTimetableUiState by viewModel.timetableUiState.collectAsStateWithLifecycle()
    val busNoticeUiState by viewModel.noticeUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    CompositionLocalProvider(LocalOnRefresh provides viewModel::refresh) {
        BusTimetableScreenContent(
            modifier = modifier,
            busTimetableUiState = busTimetableUiState,
            busNoticeUiState = busNoticeUiState,
            onNavigationIconClick = onNavigationIconClick,
            onShuttleCourseRouteClick = onNavigateToShuttleTimetableScreen,
            onExpressDirectionChange = viewModel::onExpressDirectionChanged,
            onCityBusNumberChange = viewModel::onCityBusNumberChanged,
            onCityDirectionChange = viewModel::onCityDirectionChanged,
            onCloseNotice = viewModel::closeNotice,
            onNoticeClick = { context.goToArticle(it.id) },
        )
    }
}
