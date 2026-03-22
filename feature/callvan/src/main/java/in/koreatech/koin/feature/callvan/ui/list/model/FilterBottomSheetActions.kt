package `in`.koreatech.koin.feature.callvan.ui.list.model

import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.ArrivalsFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.AuthorType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.DeparturesFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.StatusesType
import kotlinx.collections.immutable.ImmutableList

data class FilterBottomSheetActions(
    val onAuthorTypeChange: (AuthorType) -> Unit,
    val onSortTypeChange: (SortType) -> Unit,
    val onStatusesTypeChange: (StatusesType) -> Unit,
    val onDeparturesTypeChange: (ImmutableList<DeparturesFilterType>) -> Unit,
    val onArrivalsTypeChange: (ImmutableList<ArrivalsFilterType>) -> Unit,
    val onReset: () -> Unit,
    val onApplyClick: () -> Unit
)
