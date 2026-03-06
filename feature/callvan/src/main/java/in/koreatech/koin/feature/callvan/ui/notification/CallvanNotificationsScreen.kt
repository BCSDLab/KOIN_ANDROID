package `in`.koreatech.koin.feature.callvan.ui.notification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.callvan.R
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
    viewModel: CallvanNotificationViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {}
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { }

    CallvanNotificationsScreenImpl(
        notifications = state.notifications,
        onMarkAllAsRead = viewModel::markAllAsRead,
        onDeleteAll = viewModel::deleteAll,
        onTopbarBackClick = onTopbarBackClick
    )
}

@Composable
fun CallvanNotificationsScreenImpl(
    notifications: ImmutableList<CallvanNotificationUiItem>,
    onMarkAllAsRead: () -> Unit = {},
    onDeleteAll: () -> Unit = {},
    onTopbarBackClick: () -> Unit = {}
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    val menuItems = remember(onMarkAllAsRead, onDeleteAll) {
        listOf(
            DropdownMenuItem(
                text = { stringResource(R.string.callvan_notification_mark_as_read_all) },
                onClick = onMarkAllAsRead
            ),
            DropdownMenuItem(
                text = { stringResource(R.string.callvan_notification_delete_all) },
                color = { KoinTheme.colors.danger700 },
                onClick = onDeleteAll
            )
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
        LazyColumn(
            modifier = Modifier.padding(contentPadding)
        ) {
            items(notifications) { item ->
                CallvanNotificationItem(
                    notification = item
                )
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
            title = "콜밴팟 인원 모집 완료",
            routeInfo = "02.02(월) 16:00 인경관 - 천안터미널 용 8/8",
            message = "해당 콜밴팟 인원이 모두 모집되었어요. 콜밴을 예약할까요?",
            isRead = false
        ),
        CallvanNotificationUiItem(
            id = 2,
            title = "새 메시지 도착",
            routeInfo = "02.02(월) 16:00 인경관 - 천안터미널 용 7/8",
            message = "김철수: 인경 어디로 가면 되나요?",
            isRead = false
        ),
        CallvanNotificationUiItem(
            id = 3,
            title = "콜밴팟 인원 참여",
            routeInfo = "02.02(월) 16:00 인경관 - 천안터미널 용 7/8",
            message = "이훈이 님이 콜밴팟에 참여했어요.",
            isRead = true
        ),
        CallvanNotificationUiItem(
            id = 4,
            title = "콜밴팟 출발 시간 임박",
            routeInfo = "02.02(월) 16:00 인경관 - 천안터미널 용 6/8",
            message = "해당 콜밴팟 출발 시간이 30분 남았어요.",
            isRead = true
        )
    )
    CallvanNotificationsScreenImpl(
        notifications = sampleNotifications.toPersistentList()
    )
}
