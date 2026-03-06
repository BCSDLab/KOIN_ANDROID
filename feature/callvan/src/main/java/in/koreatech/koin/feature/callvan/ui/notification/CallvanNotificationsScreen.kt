package `in`.koreatech.koin.feature.callvan.ui.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanConfirmBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.notification.component.CallvanNotificationDropdownMenu
import `in`.koreatech.koin.feature.callvan.ui.notification.component.CallvanNotificationItem
import `in`.koreatech.koin.feature.callvan.ui.notification.component.DropdownMenuItem
import `in`.koreatech.koin.feature.callvan.ui.notification.model.CallvanNotificationUiItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CallvanNotificationsScreen(
    viewModel: CallvanNotificationsViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {}
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { }

    CallvanNotificationsScreenImpl(
        notifications = state.notifications,
        onMarkAsRead = viewModel::markAsRead,
        onMarkAllAsRead = viewModel::markAllAsRead,
        onDeleteAll = viewModel::deleteAll,
        onTopbarBackClick = onTopbarBackClick
    )
}

@Composable
fun CallvanNotificationsScreenImpl(
    notifications: ImmutableList<CallvanNotificationUiItem>,
    onMarkAsRead: (Int) -> Unit = {},
    onMarkAllAsRead: () -> Unit = {},
    onDeleteAll: () -> Unit = {},
    onTopbarBackClick: () -> Unit = {}
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isDeleteAllBottomSheetVisible by remember { mutableStateOf(false) }

    val menuItems = remember(onMarkAllAsRead) {
        listOf(
            DropdownMenuItem(
                text = { stringResource(R.string.callvan_notification_mark_as_read_all) },
                onClick = onMarkAllAsRead
            ),
            DropdownMenuItem(
                text = { stringResource(R.string.callvan_notification_delete_all) },
                color = { KoinTheme.colors.danger700 },
                onClick = { isDeleteAllBottomSheetVisible = true }
            )
        )
    }

    if (isDeleteAllBottomSheetVisible) {
        CallvanConfirmBottomSheet(
            title = stringResource(R.string.callvan_notification_delete_all_title),
            description = stringResource(R.string.callvan_notification_delete_all_message),
            confirmText = stringResource(R.string.callvan_notification_delete_all_confirm),
            cancelText = stringResource(R.string.callvan_notification_delete_all_cancel),
            onConfirm = {
                onDeleteAll()
                isDeleteAllBottomSheetVisible = false
            },
            onDismiss = { isDeleteAllBottomSheetVisible = false }
        )
    }

    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.callvan_notification_top_bar),
                onNavigationIconClick = onTopbarBackClick,
                actions = {
                    val density = LocalDensity.current
                    var iconButtonHeight by remember { mutableStateOf(0.dp) }
                    Box {
                        IconButton(
                            onClick = { isMenuExpanded = true },
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                iconButtonHeight = with(density) { coordinates.size.height.toDp() }
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                                contentDescription = null
                            )
                        }
                        CallvanNotificationDropdownMenu(
                            expanded = isMenuExpanded,
                            items = menuItems,
                            modifier = Modifier.padding(end = 16.dp),
                            onDismissRequest = { isMenuExpanded = false },
                            topPadding = iconButtonHeight
                        )
                    }
                }
            )
        },
        containerColor = KoinTheme.colors.neutral0
    ) { contentPadding ->
        if (notifications.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_bcsd_logo_symbol),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = androidx.compose.ui.graphics.Color.Unspecified
                )
                Text(
                    text = stringResource(R.string.callvan_notification_empty_title),
                    style = KoinTheme.typography.bold18.copy(fontWeight = FontWeight(600)),
                    color = RebrandKoinTheme.colors.primary500,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(
                    text = stringResource(R.string.callvan_notification_empty_description),
                    style = KoinTheme.typography.medium14.copy(color = KoinTheme.colors.neutral600),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(contentPadding)
            ) {
                itemsIndexed(notifications) { index, item ->
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            color = KoinTheme.colors.neutral200
                        )
                    }
                    CallvanNotificationItem(
                        notification = item,
                        onClick = onMarkAsRead
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanNotificationsScreenPreview() {
    val sampleNotifications = listOf(
        CallvanNotificationUiItem(
            id = 1,
            titleRes = R.string.callvan_notification_title_recruitment_complete,
            routeInfo = "02.02(월) 16:00 인경관 - 천안터미널",
            maxParticipants = 8,
            currentParticipants = 8,
            message = "해당 콜밴팟 인원이 모두 모집되었어요. 콜밴을 예약할까요?",
            isRead = false
        ),
        CallvanNotificationUiItem(
            id = 2,
            titleRes = R.string.callvan_notification_title_member_joined,
            routeInfo = "02.02(월) 16:00 인경관 - 천안터미널",
            maxParticipants = 8,
            currentParticipants = 7,
            message = "김철수: 인경 어디로 가면 되나요?",
            isRead = false
        ),
        CallvanNotificationUiItem(
            id = 3,
            titleRes = R.string.callvan_notification_title_new_message,
            routeInfo = "02.02(월) 16:00 인경관 - 천안터미널",
            maxParticipants = 8,
            currentParticipants = 7,
            message = "이훈이 님이 콜밴팟에 참여했어요.",
            isRead = true
        ),
        CallvanNotificationUiItem(
            id = 4,
            titleRes = R.string.callvan_notification_title_departure_imminent,
            routeInfo = "02.02(월) 16:00 인경관 - 천안터미널",
            maxParticipants = 8,
            currentParticipants = 6,
            message = "해당 콜밴팟 출발 시간이 30분 남았어요.",
            isRead = true
        )
    )
    CallvanNotificationsScreenImpl(
        notifications = sampleNotifications.toPersistentList()
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanNotificationsScreenEmptyPreview() {
    val sampleNotifications = emptyList<CallvanNotificationUiItem>()
    CallvanNotificationsScreenImpl(
        notifications = sampleNotifications.toPersistentList()
    )
}
