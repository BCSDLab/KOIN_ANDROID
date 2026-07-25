package `in`.koreatech.koin.feature.department.screen.detail

import `in`.koreatech.koin.feature.department.state.DepartmentSearchUiState
import `in`.koreatech.koin.feature.department.state.DepartmentSearchState
import `in`.koreatech.koin.feature.department.type.DepartmentCategory

data class DepartmentDetailState(
    val category: DepartmentCategory = DepartmentCategory.ACADEMIC,
    override val query: String = "",
    val updatedAt: String = "",
    val contentUiState: DepartmentSearchUiState = DepartmentSearchUiState.Loading,
    override val searchUiState: DepartmentSearchUiState = DepartmentSearchUiState.Idle
) : DepartmentSearchState<DepartmentDetailState> {
    val isSearching: Boolean get() = query.isNotBlank()

    val visibleUiState: DepartmentSearchUiState
        get() = if (isSearching) searchUiState else contentUiState

    override fun withSearch(query: String, searchUiState: DepartmentSearchUiState) =
        copy(query = query, searchUiState = searchUiState)
}

sealed interface DepartmentDetailSideEffect {
    data class CopyPhoneNumber(val phoneNumber: String) : DepartmentDetailSideEffect
}
