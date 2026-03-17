package `in`.koreatech.koin.feature.callvan.ui.list

import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanConfirmType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListUiState
import `in`.koreatech.koin.feature.callvan.ui.list.model.FilterBottomSheetState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CallvanListState(
    val items: ImmutableList<CallvanListUiState> = persistentListOf(),
    val searchValue: String = "",
    val filterState: FilterBottomSheetState = FilterBottomSheetState(),
    val hasNewNotification: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreItems: Boolean = true,
    val currentPage: Int = 1,
    val totalPage: Int = 1,
    val isLoggedIn: Boolean = false,
    val isLoginVisible: Boolean = false,
    val isFilterVisible: Boolean = false,
    val pendingConfirm: Pair<CallvanConfirmType, Int>? = null,
    val pendingCompleteIndex: Int? = null
)