package `in`.koreatech.koin.feature.callvan.ui.list

import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType
import kotlinx.collections.immutable.ImmutableList

data class FilterBottomSheetActions(
    val onSortTypeChange: (CallvanFilterType.SortType) -> Unit,
    val onStatusesTypeChange: (CallvanFilterType.StatusesType) -> Unit,
    val onDeparturesTypeChange: (ImmutableList<CallvanFilterType.DeparturesFilterType>) -> Unit,
    val onArrivalsTypeChange: (ImmutableList<CallvanFilterType.ArrivalsFilterType>) -> Unit,
    val onReset: () -> Unit,
    val onApplyClick: () -> Unit
)
