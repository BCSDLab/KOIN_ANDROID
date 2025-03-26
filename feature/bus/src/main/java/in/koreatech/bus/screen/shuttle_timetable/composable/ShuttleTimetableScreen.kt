package `in`.koreatech.bus.screen.shuttle_timetable.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.bus.screen.shuttle_timetable.viewmodel.ShuttleTimetableViewModel
import `in`.koreatech.bus.util.LocalOnRefresh

@Composable
fun ShuttleTimetableScreen(
    modifier: Modifier = Modifier,
    viewModel: ShuttleTimetableViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit = {}
) {
    val timetableUiState by viewModel.timetableUiState.collectAsState()

    CompositionLocalProvider(LocalOnRefresh provides viewModel::refresh) {
        ShuttleTimetableScreenContent(
            modifier = modifier,
            onNavigationIconClick = onNavigationIconClick,
            timetableUiState = timetableUiState
        )
    }
}
