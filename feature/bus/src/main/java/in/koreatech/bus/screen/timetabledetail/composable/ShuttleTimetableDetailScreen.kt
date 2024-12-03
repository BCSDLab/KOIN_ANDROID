package `in`.koreatech.bus.screen.timetabledetail.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.bus.screen.timetabledetail.viewmodel.ShuttleTimetableDetailViewModel

@Composable
fun ShuttleTimetableDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ShuttleTimetableDetailViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit = {}
) {

    ShuttleTimetableDetailScreenContent(
        modifier = modifier,
        onNavigationIconClick = onNavigationIconClick
    )
}