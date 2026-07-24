package `in`.koreatech.koin.feature.department.screen.detail

import `in`.koreatech.koin.feature.department.state.DepartmentSearchUiState
import `in`.koreatech.koin.feature.department.type.DepartmentCategory

data class DepartmentDetailState(
    val category: DepartmentCategory = DepartmentCategory.ACADEMIC,
    val query: String = "",
    val updatedAt: String = "",
    val contentUiState: DepartmentSearchUiState = DepartmentSearchUiState.Loading,
    val searchUiState: DepartmentSearchUiState = DepartmentSearchUiState.Idle
) {
    val isSearching: Boolean get() = query.isNotBlank()

    val visibleUiState: DepartmentSearchUiState
        get() = if (isSearching) searchUiState else contentUiState
}

sealed interface DepartmentDetailSideEffect {
    data class CopyPhoneNumber(val phoneNumber: String) : DepartmentDetailSideEffect
}
