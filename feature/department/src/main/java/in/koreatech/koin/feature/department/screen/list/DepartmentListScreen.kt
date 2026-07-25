package `in`.koreatech.koin.feature.department.screen.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.feature.department.type.DepartmentCategory
import `in`.koreatech.koin.feature.department.util.copyPhoneNumberToClipboard
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DepartmentListScreen(
    modifier: Modifier = Modifier,
    viewModel: DepartmentListViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit = {},
    navigateToDetail: (DepartmentCategory) -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is DepartmentListSideEffect.NavigateToDetail -> navigateToDetail(effect.category)
            is DepartmentListSideEffect.CopyPhoneNumber -> context.copyPhoneNumberToClipboard(effect.phoneNumber)
        }
    }

    DepartmentListScreenContent(
        modifier = modifier,
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::onSearch,
        onCategoryClick = viewModel::onCategoryClick,
        onPhoneNumberClick = viewModel::onPhoneNumberClick
    )
}
