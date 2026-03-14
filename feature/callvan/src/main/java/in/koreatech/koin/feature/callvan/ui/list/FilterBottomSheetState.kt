package `in`.koreatech.koin.feature.callvan.ui.list

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.ArrivalsFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.DeparturesFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.StatusesType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FilterBottomSheetState(
    val selectedSortType: SortType = SortType.LatestDesc,
    val selectedStatusesType: StatusesType = StatusesType.All,
    val selectedDeparturesType: ImmutableList<DeparturesFilterType> = persistentListOf(DeparturesFilterType.All),
    val selectedArrivalsType: ImmutableList<ArrivalsFilterType> = persistentListOf(ArrivalsFilterType.All)
)
