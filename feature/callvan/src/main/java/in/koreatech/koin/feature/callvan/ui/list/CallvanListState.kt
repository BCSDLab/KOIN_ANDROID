package `in`.koreatech.koin.feature.callvan.ui.list

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
    val isLoginVisible: Boolean = false
)