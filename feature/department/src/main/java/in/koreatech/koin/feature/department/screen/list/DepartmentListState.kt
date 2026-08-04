package `in`.koreatech.koin.feature.department.screen.list

import `in`.koreatech.koin.feature.department.state.DepartmentSearchState
import `in`.koreatech.koin.feature.department.state.DepartmentSearchUiState
import `in`.koreatech.koin.feature.department.type.DepartmentCategory
import kotlinx.collections.immutable.ImmutableList

data class DepartmentListState(
    override val query: String = "",
    val categories: ImmutableList<DepartmentCategory> = DepartmentCategory.ALL,
    val updatedAt: String = "",
    override val searchUiState: DepartmentSearchUiState = DepartmentSearchUiState.Idle
) : DepartmentSearchState<DepartmentListState> {
    val isSearching: Boolean get() = query.isNotBlank()

    override fun withSearch(query: String, searchUiState: DepartmentSearchUiState) =
        copy(query = query, searchUiState = searchUiState)
}

sealed interface DepartmentListSideEffect {
    data class NavigateToDetail(val category: DepartmentCategory) : DepartmentListSideEffect

    data class CopyPhoneNumber(val phoneNumber: String) : DepartmentListSideEffect
}
