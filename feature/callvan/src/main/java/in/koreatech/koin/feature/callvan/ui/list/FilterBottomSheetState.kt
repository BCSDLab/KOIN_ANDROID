package `in`.koreatech.koin.feature.callvan.ui.list

import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.ArrivalsFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.DeparturesFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.StatusesType
import kotlinx.collections.immutable.ImmutableList

data class FilterBottomSheetState(
    val selectedSortType: SortType,
    val selectedStatusesType: StatusesType,
    val selectedDeparturesType: ImmutableList<DeparturesFilterType>,
    val selectedArrivalsType: ImmutableList<ArrivalsFilterType>
)
