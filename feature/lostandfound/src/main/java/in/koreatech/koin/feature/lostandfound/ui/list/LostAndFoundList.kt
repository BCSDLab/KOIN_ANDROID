package `in`.koreatech.koin.feature.lostandfound.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.LostAndFoundContainer
import `in`.koreatech.koin.feature.lostandfound.component.LostAndFoundFAB
import `in`.koreatech.koin.feature.lostandfound.component.LostAndFoundFABBottomSheet
import `in`.koreatech.koin.feature.lostandfound.component.LostAndFoundFilterBottomSheet
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun LostAndFoundList(
    viewModel: LostAndFoundListViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    if (uiState.showFilterBottomSheet) {
        LostAndFoundFilterBottomSheet(
            onDismissRequest = {
                viewModel.setShowFilterBottomSheet(false)
            },
            onApply = { a, b, c, d -> } // TODO connect viewModel
        )
    }

    if (uiState.showWriteBottomSheet) {
        LostAndFoundFABBottomSheet(
            onDismissRequest = {
                viewModel.setShowWriteBottomSheet(false)
            },
            onFindOwnerClick = {}, // TODO connect viewModel
            onLostItemClick = {} // TODO connect viewModel
        )
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.lost_and_found),
                onNavigationIconClick = onTopbarBackClick,
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(24.dp)
                            .noRippleClickable(onClick = onSearchClick),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_search_vector),
                        contentDescription = ""
                    )
                }
            )
        },
        floatingActionButton = {
            LostAndFoundFAB(
                onClick = {
                    viewModel.setShowWriteBottomSheet(true)
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
        ) {
            LostAndFoundContainer(
                onFilterClick = {
                    viewModel.setShowFilterBottomSheet(true)
                }
            )
        }
    }
}
