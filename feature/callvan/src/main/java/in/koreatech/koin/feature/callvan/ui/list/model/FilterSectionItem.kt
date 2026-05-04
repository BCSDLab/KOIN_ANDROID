package `in`.koreatech.koin.feature.callvan.ui.list.model

import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.ArrivalsFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.DeparturesFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.ListType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.StatusesType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

val LIST_TYPE_ITEMS = persistentListOf(ListType.All, ListType.My, ListType.Joined)
val SORT_TYPE_ITEMS = persistentListOf(SortType.LatestDesc, SortType.LatestAsc, SortType.DepartureDesc, SortType.DepartureAsc)
val STATUSES_TYPE_ITEMS = persistentListOf(StatusesType.All, StatusesType.Recruiting, StatusesType.Closed, StatusesType.Completed)
val DEPARTURES_ITEMS = persistentListOf(
    DeparturesFilterType.All, DeparturesFilterType.FrontGate,
    DeparturesFilterType.BackGate, DeparturesFilterType.TennisCourt,
    DeparturesFilterType.DormitoryMain, DeparturesFilterType.DormitorySub,
    DeparturesFilterType.Terminal, DeparturesFilterType.Station,
    DeparturesFilterType.AsanStation
)
val ARRIVALS_ITEMS = persistentListOf(
    ArrivalsFilterType.All, ArrivalsFilterType.FrontGate,
    ArrivalsFilterType.BackGate, ArrivalsFilterType.TennisCourt,
    ArrivalsFilterType.DormitoryMain, ArrivalsFilterType.DormitorySub,
    ArrivalsFilterType.Terminal, ArrivalsFilterType.Station,
    ArrivalsFilterType.AsanStation
)

data class FilterSectionItem(
    val title: String,
    val items: ImmutableList<CallvanFilterType>,
    val selectedItems: ImmutableList<CallvanFilterType>,
    val hint: String? = null
)
