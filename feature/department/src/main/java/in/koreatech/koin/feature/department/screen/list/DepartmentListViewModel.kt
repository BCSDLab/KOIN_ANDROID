package `in`.koreatech.koin.feature.department.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.domain.repository.DepartmentRepository
import `in`.koreatech.koin.feature.department.state.DepartmentSearchUiState
import `in`.koreatech.koin.feature.department.state.toDepartmentState
import `in`.koreatech.koin.feature.department.type.DepartmentCategory
import `in`.koreatech.koin.feature.department.util.DEPARTMENT_UPDATED_AT_FORMATTER
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class DepartmentListViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository
) : ViewModel(), ContainerHost<DepartmentListState, DepartmentListSideEffect> {

    override val container =
        container<DepartmentListState, DepartmentListSideEffect>(DepartmentListState()) {
            fetchUpdatedAt()
        }

    private var searchJob: Job? = null

    private fun fetchUpdatedAt() = intent {
        departmentRepository.getDepartmentContacts()
            .onSuccess { result ->
                reduce { state.copy(updatedAt = result.updatedAt.format(DEPARTMENT_UPDATED_AT_FORMATTER)) }
            }
    }

    fun onQueryChange(query: String) {
        searchJob?.cancel()

        if (query.isBlank()) {
            intent {
                reduce {
                    state.copy(query = query, searchUiState = DepartmentSearchUiState.Idle)
                }
            }
            return
        }

        intent {
            reduce {
                state.copy(query = query, searchUiState = DepartmentSearchUiState.Loading)
            }
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            search(query)
        }
    }

    fun onSearch() {
        searchJob?.cancel()
        val query = container.stateFlow.value.query
        if (query.isBlank()) return
        EventLogger.logCampusClickEvent(EVENT_LABEL_SEARCH, query)
        searchJob = viewModelScope.launch { search(query) }
    }

    private fun search(keyword: String) = intent {
        departmentRepository.getDepartmentContacts(keyword = keyword)
            .onSuccess { result ->
                val results = result.categories
                    .flatMap { it.departments }
                    .map { it.toDepartmentState() }
                reduce {
                    state.copy(
                        searchUiState = if (results.isEmpty()) {
                            DepartmentSearchUiState.Empty
                        } else {
                            DepartmentSearchUiState.Success(results.toImmutableList())
                        }
                    )
                }
            }
            .onFailure {
                reduce { state.copy(searchUiState = DepartmentSearchUiState.Failure) }
            }
    }

    fun onCategoryClick(category: DepartmentCategory) = intent {
        EventLogger.logCampusClickEvent(EVENT_LABEL_CATEGORY, category.loggingValue)
        postSideEffect(DepartmentListSideEffect.NavigateToDetail(category))
    }

    fun onPhoneNumberClick(phoneNumber: String) = intent {
        EventLogger.logCampusClickEvent(EVENT_LABEL_COPY, phoneNumber)
        postSideEffect(DepartmentListSideEffect.CopyPhoneNumber(phoneNumber))
    }

    fun onRefresh() {
        val query = container.stateFlow.value.query
        if (query.isBlank()) fetchUpdatedAt() else onSearch()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 300L
        private const val EVENT_LABEL_SEARCH = "department_search"
        private const val EVENT_LABEL_CATEGORY = "department_category"
        private const val EVENT_LABEL_COPY = "department_copy_phone_number"
    }
}
