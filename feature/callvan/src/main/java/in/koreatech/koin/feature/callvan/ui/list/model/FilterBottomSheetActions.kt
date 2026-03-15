package `in`.koreatech.koin.feature.callvan.ui.list.model

import kotlinx.collections.immutable.ImmutableList

data class FilterBottomSheetActions(
    val onSortTypeChange: (CallvanFilterType) -> Unit,
    val onStatusesTypeChange: (CallvanFilterType) -> Unit,
    val onDeparturesTypeChange: (ImmutableList<CallvanFilterType>) -> Unit,
    val onArrivalsTypeChange: (ImmutableList<CallvanFilterType>) -> Unit,
    val onReset: () -> Unit,
    val onApplyClick: () -> Unit
)