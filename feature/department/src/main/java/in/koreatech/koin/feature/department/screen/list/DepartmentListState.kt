package `in`.koreatech.koin.feature.department.screen.list

import `in`.koreatech.koin.feature.department.state.DepartmentSearchUiState
import `in`.koreatech.koin.feature.department.type.DepartmentCategory
import kotlinx.collections.immutable.ImmutableList

data class DepartmentListState(
    val query: String = "",
    val categories: ImmutableList<DepartmentCategory> = DepartmentCategory.ALL,
    val updatedAt: String = "",
    val searchUiState: DepartmentSearchUiState = DepartmentSearchUiState.Idle
) {
    val isSearching: Boolean get() = query.isNotBlank()
}

sealed interface DepartmentListSideEffect {
    data class NavigateToDetail(val category: DepartmentCategory) : DepartmentListSideEffect

    data class CopyPhoneNumber(val phoneNumber: String) : DepartmentListSideEffect
}
