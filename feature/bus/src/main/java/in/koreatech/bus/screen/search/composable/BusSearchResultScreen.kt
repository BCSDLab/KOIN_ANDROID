package `in`.koreatech.bus.screen.search.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.bus.screen.search.viewmodel.BusSearchResultViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusSearchResultScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    viewModel: BusSearchResultViewModel = hiltViewModel()
) {

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title = "한기대 → 천안터미널", // TODO : 방향
            onNavigationIconClick = onNavigationIconClick
        )

    }

}