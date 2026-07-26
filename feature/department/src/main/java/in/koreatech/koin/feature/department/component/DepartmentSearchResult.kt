package `in`.koreatech.koin.feature.department.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.mock.departmentsPreviewMock
import `in`.koreatech.koin.feature.department.state.DepartmentSearchUiState
import `in`.koreatech.koin.feature.department.state.DepartmentState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DepartmentSearchResult(
    uiState: DepartmentSearchUiState,
    modifier: Modifier = Modifier,
    onPhoneNumberClick: (String) -> Unit = {}
) {
    when (uiState) {
        is DepartmentSearchUiState.Idle -> Unit

        is DepartmentSearchUiState.Loading ->
            DepartmentLoadingList(modifier = modifier)

        is DepartmentSearchUiState.Success ->
            DepartmentCardList(
                modifier = modifier,
                departments = uiState.departments,
                onPhoneNumberClick = onPhoneNumberClick
            )

        is DepartmentSearchUiState.Empty,
        is DepartmentSearchUiState.Failure ->
            DepartmentStateView(modifier = modifier.padding(top = 80.dp))
    }
}

@Composable
internal fun DepartmentCardList(
    departments: ImmutableList<DepartmentState>,
    modifier: Modifier = Modifier,
    onPhoneNumberClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        departments.forEachIndexed { index, department ->
            key(index, department.name) {
                DepartmentCard(
                    department = department,
                    onPhoneNumberClick = onPhoneNumberClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentSearchResultPreview() {
    RebrandKoinTheme {
        DepartmentSearchResult(
            modifier = Modifier.padding(16.dp),
            uiState = DepartmentSearchUiState.Success(departmentsPreviewMock)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentSearchEmptyPreview() {
    RebrandKoinTheme {
        DepartmentSearchResult(
            modifier = Modifier.padding(16.dp),
            uiState = DepartmentSearchUiState.Empty
        )
    }
}
