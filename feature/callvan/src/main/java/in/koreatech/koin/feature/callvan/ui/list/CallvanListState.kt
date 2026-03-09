package `in`.koreatech.koin.feature.callvan.ui.list

import android.os.Parcelable
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.ArrivalsFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.DeparturesFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.StatusesType
import `in`.koreatech.koin.feature.callvan.enums.ConfirmType
import `in`.koreatech.koin.feature.callvan.model.CallvanListUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class CallvanListState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isFirstPageLoading: Boolean = true,
    val searchQuery: String = "",
    val sortType: SortType = SortType.LatestDesc,
    val statusesType: StatusesType = StatusesType.All,
    val departuresFilterType: ImmutableList<DeparturesFilterType> = persistentListOf(DeparturesFilterType.All),
    val arrivalsFilterType: ImmutableList<ArrivalsFilterType> = persistentListOf(ArrivalsFilterType.All),
    val articles: ImmutableList<CallvanListUiState> = persistentListOf(),
    val currentPage: Int = 1,
    val totalPage: Int = 1,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val showFilterBottomSheet: Boolean = false,
    val showLoginBottomSheet: Boolean = false,
    val showConfirmBottomSheet: Boolean = false,
    val showCompleteBottomSheet: Boolean = false,
    val confirmType: ConfirmType? = null,
    val selectedItemId: Int? = null
) : Parcelable
