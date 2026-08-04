package `in`.koreatech.koin.feature.department.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.domain.usecase.department.GetDepartmentContactsByCategoryUseCase
import `in`.koreatech.koin.feature.department.navigation.Routes
import `in`.koreatech.koin.feature.department.state.DepartmentSearchFieldHandler
import `in`.koreatech.koin.feature.department.state.DepartmentSearchUiState
import `in`.koreatech.koin.feature.department.state.toDepartmentState
import `in`.koreatech.koin.feature.department.util.DEPARTMENT_UPDATED_AT_FORMATTER
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class DepartmentDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDepartmentContactsByCategoryUseCase: GetDepartmentContactsByCategoryUseCase
) : ViewModel(), ContainerHost<DepartmentDetailState, DepartmentDetailSideEffect> {

    private val category = savedStateHandle.toRoute<Routes.DepartmentDetail>().category

    override val container =
        container<DepartmentDetailState, DepartmentDetailSideEffect>(
            DepartmentDetailState(category = category)
        ) {
            fetchDepartments()
        }

    private val searchFieldHandler = DepartmentSearchFieldHandler(
        host = this,
        scope = viewModelScope,
        fetch = { keyword ->
            getDepartmentContactsByCategoryUseCase(category = category.name, keyword = keyword)
                .map { result -> result.categoryContacts.departments.map { it.toDepartmentState() } }
        },
        onSearchLog = { keyword -> EventLogger.logCampusClickEvent(AnalyticsConstant.Label.Department.DEPARTMENT_SEARCH, keyword) }
    )

    private fun fetchDepartments() = intent {
        reduce { state.copy(contentUiState = DepartmentSearchUiState.Loading) }

        getDepartmentContactsByCategoryUseCase(category = category.name)
            .onSuccess { result ->
                val departmentStates = result.categoryContacts.departments.map { it.toDepartmentState() }
                reduce {
                    state.copy(
                        updatedAt = result.updatedAt.format(DEPARTMENT_UPDATED_AT_FORMATTER),
                        contentUiState = if (departmentStates.isEmpty()) {
                            DepartmentSearchUiState.Empty
                        } else {
                            DepartmentSearchUiState.Success(departmentStates.toImmutableList())
                        }
                    )
                }
            }
            .onFailure {
                reduce { state.copy(contentUiState = DepartmentSearchUiState.Failure) }
            }
    }

    fun onQueryChange(query: String) = searchFieldHandler.onQueryChange(query)

    fun onSearch() = searchFieldHandler.onSearch()

    fun onPhoneNumberClick(phoneNumber: String) = intent {
        EventLogger.logCampusClickEvent(AnalyticsConstant.Label.Department.DEPARTMENT_CONTACT_COPY, "전화번호 복사")
        postSideEffect(DepartmentDetailSideEffect.CopyPhoneNumber(phoneNumber))
    }
}
