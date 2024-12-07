package `in`.koreatech.bus.screen.shuttle_timetable.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.bus.screen.shuttle_timetable.viewmodel.ShuttleTimetableDetailViewModel

@Composable
fun ShuttleTimetableScreen(
    modifier: Modifier = Modifier,
    viewModel: ShuttleTimetableDetailViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit = {}
) {

    val timetableUiState by viewModel.timetableUiState.collectAsState()

    ShuttleTimetableScreenContent(
        modifier = modifier,
        onNavigationIconClick = onNavigationIconClick,
        timetableUiState = timetableUiState
    )
}