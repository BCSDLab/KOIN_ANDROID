package `in`.koreatech.koin.feature.callvan.ui.list.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FilterBottomSheetState(
    val selectedAuthorType: CallvanFilterType.AuthorType = CallvanFilterType.AuthorType.All,
    val selectedSortType: CallvanFilterType.SortType = CallvanFilterType.SortType.LatestDesc,
    val selectedStatusesType: CallvanFilterType.StatusesType = CallvanFilterType.StatusesType.All,
    val selectedDeparturesType: ImmutableList<CallvanFilterType.DeparturesFilterType> = persistentListOf(
        CallvanFilterType.DeparturesFilterType.All
    ),
    val selectedArrivalsType: ImmutableList<CallvanFilterType.ArrivalsFilterType> = persistentListOf(
        CallvanFilterType.ArrivalsFilterType.All
    )
)
