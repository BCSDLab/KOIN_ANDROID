package `in`.koreatech.koin.feature.recruitment.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.recruitment.GetRecruitmentsUseCase
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentSort
import `in`.koreatech.koin.feature.recruitment.ui.main.model.toRecruitmentItemModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
@Suppress("TooManyFunctions")
class RecruitmentMainViewModel @Inject constructor(
    private val getRecruitmentsUseCase: GetRecruitmentsUseCase
) : ViewModel(),
    ContainerHost<RecruitmentMainState, RecruitmentMainSideEffect> {

    override val container = container<RecruitmentMainState, RecruitmentMainSideEffect>(
        RecruitmentMainState()
    )

    private var searchJob: Job? = null

    fun fetchRecruitments(isRefresh: Boolean = false) = intent {
        reduce {
            if (isRefresh) state.copy(isRefreshing = true) else state.copy(isLoading = true)
        }
        val filter = state.filterState
        getRecruitmentsUseCase(
            keyword = state.searchValue.takeIf { it.isNotBlank() },
            status = filter.selectedStatus?.apiValue,
            categories = filter.selectedCategories
                .takeIf { it.isNotEmpty() }
                ?.map { it.apiValue },
            meetingType = filter.selectedLocation?.apiValue,
            sort = filter.selectedSort.apiValue
        ).onSuccess { recruitments ->
            reduce {
                state.copy(
                    items = recruitments.recruitments
                        .map { it.toRecruitmentItemModel() }
                        .toImmutableList(),
                    totalCount = recruitments.totalCount,
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }.onFailure {
            reduce { state.copy(isLoading = false, isRefreshing = false) }
            postSideEffect(RecruitmentMainSideEffect.ShowError)
        }
    }

    fun updateSearch(query: String) = blockingIntent {
        reduce { state.copy(searchValue = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            fetchRecruitments()
        }
    }

    fun updateFilterVisible(visible: Boolean) = blockingIntent {
        reduce {
            if (visible) {
                state.copy(isFilterVisible = true, pendingFilterState = state.filterState)
            } else {
                state.copy(isFilterVisible = false)
            }
        }
    }

    fun selectPendingStatus(status: RecruitmentStatus?) =
        updatePendingFilter { it.copy(selectedStatus = status) }

    fun selectPendingSort(sort: RecruitmentSort) =
        updatePendingFilter { it.copy(selectedSort = sort) }

    fun togglePendingCategory(category: RecruitmentCategory?) =
        updatePendingFilter { it.copy(selectedCategories = it.selectedCategories.toggle(category)) }

    fun selectPendingLocation(location: RecruitmentLocation?) =
        updatePendingFilter { it.copy(selectedLocation = location) }

    fun resetPendingFilter() = updatePendingFilter { RecruitmentFilterState() }

    fun applyPendingFilter() = intent {
        reduce { state.copy(filterState = state.pendingFilterState, isFilterVisible = false) }
        fetchRecruitments()
    }

    fun removeStatusFilter() = updateFilter { it.copy(selectedStatus = null) }

    fun removeCategoryFilter(category: RecruitmentCategory) =
        updateFilter { it.copy(selectedCategories = (it.selectedCategories - category).toPersistentList()) }

    fun removeLocationFilter() = updateFilter { it.copy(selectedLocation = null) }

    private fun updatePendingFilter(transform: (RecruitmentFilterState) -> RecruitmentFilterState) =
        blockingIntent {
            reduce { state.copy(pendingFilterState = transform(state.pendingFilterState)) }
        }

    private fun updateFilter(transform: (RecruitmentFilterState) -> RecruitmentFilterState) =
        intent {
            reduce { state.copy(filterState = transform(state.filterState)) }
            fetchRecruitments()
        }

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 300L
    }
}

private fun <T> ImmutableList<T>.toggle(item: T?): ImmutableList<T> = when {
    item == null -> persistentListOf()
    item in this -> (this - item).toPersistentList()
    else -> (this + item).toPersistentList()
}
