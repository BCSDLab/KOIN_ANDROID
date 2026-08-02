package `in`.koreatech.koin.feature.department.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.mock.departmentsPreviewMock
import `in`.koreatech.koin.feature.department.state.DepartmentSearchUiState

@Composable
internal fun rememberDisplayedSearchUiState(
    uiState: DepartmentSearchUiState
): DepartmentSearchUiState {
    var displayed by remember { mutableStateOf(uiState) }
    if (uiState !is DepartmentSearchUiState.Loading) {
        displayed = uiState
    }
    return displayed
}

@Composable
internal fun DepartmentSearchResultContent(
    uiState: DepartmentSearchUiState,
    modifier: Modifier = Modifier,
    onPhoneNumberClick: (String) -> Unit = {}
) {
    when (uiState) {
        is DepartmentSearchUiState.Idle,
        is DepartmentSearchUiState.Loading -> Unit

        is DepartmentSearchUiState.Success ->
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.departments.forEach { department ->
                    DepartmentCard(
                        department = department,
                        onPhoneNumberClick = onPhoneNumberClick
                    )
                }
            }

        is DepartmentSearchUiState.Empty,
        is DepartmentSearchUiState.Failure ->
            DepartmentStateView(modifier = modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentSearchResultPreview() {
    RebrandKoinTheme {
        DepartmentSearchResultContent(
            modifier = Modifier.padding(16.dp),
            uiState = DepartmentSearchUiState.Success(departmentsPreviewMock)
        )
    }
}

@Preview(showBackground = true, heightDp = 500)
@Composable
private fun DepartmentSearchEmptyPreview() {
    RebrandKoinTheme {
        DepartmentSearchResultContent(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            uiState = DepartmentSearchUiState.Empty
        )
    }
}
