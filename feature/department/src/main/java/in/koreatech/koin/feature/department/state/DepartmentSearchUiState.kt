package `in`.koreatech.koin.feature.department.state

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface DepartmentSearchUiState {
    data object Idle : DepartmentSearchUiState

    data object Loading : DepartmentSearchUiState

    data class Success(val departments: ImmutableList<DepartmentState>) : DepartmentSearchUiState

    data object Empty : DepartmentSearchUiState

    data object Failure : DepartmentSearchUiState
}
