package `in`.koreatech.koin.feature.department.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.domain.usecase.department.GetDepartmentContactsUseCase
import `in`.koreatech.koin.feature.department.state.DepartmentSearchFieldHandler
import `in`.koreatech.koin.feature.department.state.toDepartmentState
import `in`.koreatech.koin.feature.department.type.DepartmentCategory
import `in`.koreatech.koin.feature.department.util.DEPARTMENT_UPDATED_AT_FORMATTER
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class DepartmentListViewModel @Inject constructor(
    private val getDepartmentContactsUseCase: GetDepartmentContactsUseCase
) : ViewModel(), ContainerHost<DepartmentListState, DepartmentListSideEffect> {

    override val container =
        container<DepartmentListState, DepartmentListSideEffect>(DepartmentListState()) {
            fetchUpdatedAt()
        }

    private val searchFieldHandler = DepartmentSearchFieldHandler(
        host = this,
        scope = viewModelScope,
        fetch = { keyword ->
            getDepartmentContactsUseCase(keyword = keyword).map { result ->
                result.categories
                    .flatMap { it.departments }
                    .map { it.toDepartmentState() }
            }
        },
        onSearchLog = { keyword -> EventLogger.logCampusClickEvent(EVENT_LABEL_SEARCH, keyword) }
    )

    private fun fetchUpdatedAt() = intent {
        getDepartmentContactsUseCase()
            .onSuccess { result ->
                reduce { state.copy(updatedAt = result.updatedAt.format(DEPARTMENT_UPDATED_AT_FORMATTER)) }
            }
    }

    fun onQueryChange(query: String) = searchFieldHandler.onQueryChange(query)

    fun onSearch() = searchFieldHandler.onSearch()

    fun onCategoryClick(category: DepartmentCategory) = intent {
        EventLogger.logCampusClickEvent(EVENT_LABEL_CATEGORY, category.loggingValue)
        postSideEffect(DepartmentListSideEffect.NavigateToDetail(category))
    }

    fun onPhoneNumberClick(phoneNumber: String) = intent {
        EventLogger.logCampusClickEvent(EVENT_LABEL_COPY, phoneNumber)
        postSideEffect(DepartmentListSideEffect.CopyPhoneNumber(phoneNumber))
    }

    companion object {
        private const val EVENT_LABEL_SEARCH = "department_search"
        private const val EVENT_LABEL_CATEGORY = "department_category"
        private const val EVENT_LABEL_COPY = "department_copy_phone_number"
    }
}
