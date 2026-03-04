package `in`.koreatech.koin.feature.callvan.ui.notification

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.callvan.ui.notification.component.CallvanNotificationItem
import `in`.koreatech.koin.feature.callvan.ui.notification.component.CallvanNotificationUiItem

@Composable
fun CallvanNotificationScreen(
    notifications: List<CallvanNotificationUiItem>,
    onNavigationIconClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = "알림",
                onNavigationIconClick = onNavigationIconClick,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "더보기"
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
private fun CallvanNotificationScreenPreview() {
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
    CallvanNotificationScreen(
        notifications = sampleNotifications
    )
}
