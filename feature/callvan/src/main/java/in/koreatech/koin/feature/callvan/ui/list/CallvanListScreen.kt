package `in`.koreatech.koin.feature.callvan.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanConfirmBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanNotificationIcon
import `in`.koreatech.koin.feature.callvan.ui.list.component.CallvanFAB
import `in`.koreatech.koin.feature.callvan.ui.list.component.CallvanFilterChip
import `in`.koreatech.koin.feature.callvan.ui.list.component.CallvanListItem
import `in`.koreatech.koin.feature.callvan.ui.list.component.FilterBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.list.component.ItemSearchTextField
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanConfirmType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanItemState
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListItemClickListener
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListUiState
import `in`.koreatech.koin.feature.callvan.ui.list.model.FilterBottomSheetState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Suppress("LongParameterList")
@Composable
fun CallvanListScreenImpl(
    searchValue: String,
    items: ImmutableList<CallvanListUiState>,
    filterState: FilterBottomSheetState = FilterBottomSheetState(),
    hasNewNotification: Boolean = false,
    isLoginVisible: Boolean = false,
    onSearchValueChange: (String) -> Unit = {},
    onFilterApply: (
        CallvanFilterType.SortType,
        CallvanFilterType.StatusesType,
        ImmutableList<CallvanFilterType.DeparturesFilterType>,
        ImmutableList<CallvanFilterType.ArrivalsFilterType>
    ) -> Unit = { _, _, _, _ -> },
    onTopbarBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onWriteClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLoginDismiss: () -> Unit = {},
    onJoin: (Int) -> Unit = {},
    onCancelJoin: (Int) -> Unit = {},
    onClose: (Int) -> Unit = {},
    onReRecruit: (Int) -> Unit = {},
    onComplete: (Int) -> Unit = {},
    onCall: (Int) -> Unit = {},
    onChat: (Int) -> Unit = {}
) {
    var isFilterVisible by remember { mutableStateOf(false) }
    var pendingConfirm: Pair<CallvanConfirmType, Int>? by remember { mutableStateOf(null) }
    var pendingCompleteIndex: Int? by remember { mutableStateOf(null) }

    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.callvan_detail_top_bar),
                onNavigationIconClick = onTopbarBackClick,
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        CallvanNotificationIcon(hasNewNotification = hasNewNotification)
                    }
                }
            )
        },
        floatingActionButton = {
            CallvanFAB(
                modifier = Modifier.padding(bottom = 16.dp),
                onClick = onWriteClick
            )
        },
        containerColor = KoinTheme.colors.neutral0
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ItemSearchTextField(
                        value = searchValue,
                        onValueChange = onSearchValueChange,
                        modifier = Modifier.weight(1f)
                    )
                    CallvanFilterChip(onClick = { isFilterVisible = true })
                }
            }

            itemsIndexed(items) { index, uiState ->
                CallvanListItem(
                    uiState = uiState,
                    clickListener = object : CallvanListItemClickListener {
                        override fun onJoin() {
                            pendingConfirm = Pair(CallvanConfirmType.JOIN, index)
                        }
                        override fun onCancelJoin() {
                            pendingConfirm = Pair(CallvanConfirmType.CANCEL_JOIN, index)
                        }
                        override fun onClose() {
                            pendingConfirm = Pair(CallvanConfirmType.CLOSE, index)
                        }
                        override fun onReRecruit() {
                            pendingConfirm = Pair(CallvanConfirmType.REOPEN, index)
                        }
                        override fun onComplete() {
                            pendingCompleteIndex = index
                        }
                        override fun onCall() { onCall(index) }
                        override fun onChat() { onChat(index) }
                    }
                )
            }
        }

        if (isFilterVisible) {
            FilterBottomSheet(
                onDismissRequest = { isFilterVisible = false },
                selectedSortType = filterState.selectedSortType,
                selectedStatusesType = filterState.selectedStatusesType,
                selectedArrivalsType = filterState.selectedArrivalsType,
                selectedDeparturesType = filterState.selectedDeparturesType,
                onApply = onFilterApply
            )
        }

        pendingConfirm?.let { (confirmType, index) ->
            val title = when (confirmType) {
                CallvanConfirmType.JOIN -> stringResource(R.string.callvan_confirm_join_title)
                CallvanConfirmType.CANCEL_JOIN -> stringResource(R.string.callvan_confirm_cancel_title)
                CallvanConfirmType.CLOSE -> stringResource(R.string.callvan_confirm_close_title)
                CallvanConfirmType.REOPEN -> stringResource(R.string.callvan_confirm_reopen_title)
            }
            CallvanConfirmBottomSheet(
                title = title,
                description = "",
                confirmText = stringResource(R.string.callvan_confirm_positive),
                cancelText = stringResource(R.string.callvan_confirm_negative),
                onConfirm = {
                    when (confirmType) {
                        CallvanConfirmType.JOIN -> onJoin(index)
                        CallvanConfirmType.CANCEL_JOIN -> onCancelJoin(index)
                        CallvanConfirmType.CLOSE -> onClose(index)
                        CallvanConfirmType.REOPEN -> onReRecruit(index)
                    }
                    pendingConfirm = null
                },
                onDismiss = { pendingConfirm = null }
            )
        }

        pendingCompleteIndex?.let { index ->
            CallvanConfirmBottomSheet(
                title = stringResource(R.string.callvan_complete_title),
                description = stringResource(R.string.callvan_complete_description),
                confirmText = stringResource(R.string.callvan_confirm_positive),
                cancelText = stringResource(R.string.callvan_confirm_negative),
                onConfirm = {
                    onComplete(index)
                    pendingCompleteIndex = null
                },
                onDismiss = { pendingCompleteIndex = null }
            )
        }

        if (isLoginVisible) {
            CallvanConfirmBottomSheet(
                title = stringResource(R.string.callvan_login_title),
                description = "",
                confirmText = stringResource(R.string.callvan_login_login),
                cancelText = stringResource(R.string.callvan_login_close),
                onConfirm = onLoginClick,
                onDismiss = onLoginDismiss
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanListScreenPreview() {
    RebrandKoinTheme {
        CallvanListScreenImpl(
            searchValue = "",
            items = persistentListOf(
                CallvanListUiState("테니스장", "천안 시외터미널", "2025-02-05", "14:00", 1, 8, CallvanItemState.DEFAULT),
                CallvanListUiState("정문", "천안 시외터미널", "2025-02-05", "14:00", 1, 8, CallvanItemState.JOINED),
                CallvanListUiState("테니스장", "천안역", "2025-02-05", "14:00", 1, 8, CallvanItemState.CLOSED),
                CallvanListUiState("담헌 앞", "천안아산역", "2025-02-05", "14:00", 1, 8, CallvanItemState.OWNER_ACTIVE),
                CallvanListUiState("천안 시외터미널", "학교", "2025-02-05", "14:00", 1, 8, CallvanItemState.OWNER_CLOSED)
            )
        )
    }
}