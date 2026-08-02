package `in`.koreatech.koin.feature.department.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.component.DepartmentFooter
import `in`.koreatech.koin.feature.department.component.DepartmentSearchField
import `in`.koreatech.koin.feature.department.component.DepartmentSearchResultContent
import `in`.koreatech.koin.feature.department.component.rememberDisplayedSearchUiState
import `in`.koreatech.koin.feature.department.mock.departmentsPreviewMock
import `in`.koreatech.koin.feature.department.state.DepartmentSearchUiState
import `in`.koreatech.koin.feature.department.type.DepartmentCategory

private val SCREEN_HORIZONTAL_PADDING = 24.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DepartmentDetailScreenContent(
    uiState: DepartmentDetailState,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onPhoneNumberClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.background(Color(0xFFF8F8FA))
    ) {
        KoinTopAppBar(
            title = stringResource(uiState.category.titleRes),
            onNavigationIconClick = onNavigationIconClick,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF8F8FA)
            )
        )

        val displayedUiState = rememberDisplayedSearchUiState(uiState.visibleUiState)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .imePadding()
                .padding(horizontal = SCREEN_HORIZONTAL_PADDING),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                val shouldFillRemainingHeight = displayedUiState is DepartmentSearchUiState.Empty ||
                    displayedUiState is DepartmentSearchUiState.Failure

                Column(
                    modifier = if (shouldFillRemainingHeight) {
                        Modifier.fillParentMaxHeight()
                    } else {
                        Modifier
                    },
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DepartmentSearchField(
                        modifier = Modifier.padding(top = 8.dp),
                        query = uiState.query,
                        onQueryChange = onQueryChange,
                        onSearch = onSearch
                    )

                    DepartmentSearchResultContent(
                        modifier = if (shouldFillRemainingHeight) {
                            Modifier.weight(1f)
                        } else {
                            Modifier
                        },
                        uiState = displayedUiState,
                        onPhoneNumberClick = onPhoneNumberClick
                    )
                }
            }

            item {
                Column {
                    DepartmentFooter(
                        modifier = Modifier.padding(vertical = 12.dp),
                        updatedAt = uiState.updatedAt
                    )

                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentDetailScreenPreview() {
    RebrandKoinTheme {
        DepartmentDetailScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = DepartmentDetailState(
                category = DepartmentCategory.ACADEMIC,
                updatedAt = "2026.06.24",
                contentUiState = DepartmentSearchUiState.Success(departmentsPreviewMock)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentDetailScreenEmptyPreview() {
    RebrandKoinTheme {
        DepartmentDetailScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = DepartmentDetailState(
                category = DepartmentCategory.ACADEMIC,
                query = "ㅁㄴㅇㄹ",
                updatedAt = "2026.06.24",
                searchUiState = DepartmentSearchUiState.Empty
            )
        )
    }
}
