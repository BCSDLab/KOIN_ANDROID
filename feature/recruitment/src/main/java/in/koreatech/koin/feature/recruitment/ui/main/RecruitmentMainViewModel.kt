package `in`.koreatech.koin.feature.recruitment.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentSort
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentStatus
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
@Suppress("TooManyFunctions")
class RecruitmentMainViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<RecruitmentMainState, RecruitmentMainSideEffect> {

    override val container = container<RecruitmentMainState, RecruitmentMainSideEffect>(
        RecruitmentMainState()
    )

    fun updateSearch(query: String) = blockingIntent {
        reduce { state.copy(searchValue = query) }
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

    fun togglePendingLocation(location: RecruitmentLocation?) =
        updatePendingFilter { it.copy(selectedLocations = it.selectedLocations.toggle(location)) }

    fun resetPendingFilter() = updatePendingFilter { RecruitmentFilterState() }

    fun applyPendingFilter() = blockingIntent {
        reduce { state.copy(filterState = state.pendingFilterState, isFilterVisible = false) }
    }

    fun removeStatusFilter() = updateFilter { it.copy(selectedStatus = null) }

    fun removeCategoryFilter(category: RecruitmentCategory) =
        updateFilter { it.copy(selectedCategories = (it.selectedCategories - category).toPersistentList()) }

    fun removeLocationFilter(location: RecruitmentLocation) =
        updateFilter { it.copy(selectedLocations = (it.selectedLocations - location).toPersistentList()) }

    private fun updatePendingFilter(transform: (RecruitmentFilterState) -> RecruitmentFilterState) =
        blockingIntent {
            reduce { state.copy(pendingFilterState = transform(state.pendingFilterState)) }
        }

    private fun updateFilter(transform: (RecruitmentFilterState) -> RecruitmentFilterState) =
        blockingIntent {
            reduce { state.copy(filterState = transform(state.filterState)) }
        }
}

private fun <T> ImmutableList<T>.toggle(item: T?): ImmutableList<T> = when {
    item == null -> persistentListOf()
    item in this -> (this - item).toPersistentList()
    else -> (this + item).toPersistentList()
}
