package `in`.koreatech.koin.feature.department.screen.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.feature.department.util.copyPhoneNumberToClipboard
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DepartmentDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DepartmentDetailViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is DepartmentDetailSideEffect.CopyPhoneNumber -> context.copyPhoneNumberToClipboard(effect.phoneNumber)
        }
    }

    DepartmentDetailScreenContent(
        modifier = modifier,
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::onSearch,
        onPhoneNumberClick = viewModel::onPhoneNumberClick,
        onRefresh = viewModel::onRefresh
    )
}
