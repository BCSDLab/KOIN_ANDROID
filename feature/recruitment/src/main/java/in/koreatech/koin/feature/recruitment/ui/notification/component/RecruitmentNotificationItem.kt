package `in`.koreatech.koin.feature.recruitment.ui.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.notification.model.RecruitmentNotificationCategory
import kotlin.math.roundToInt

private enum class SwipeAnchor { Closed, Open, Dismissed }

@Composable
internal fun RecruitmentNotificationItem(
    title: String,
    content: String,
    timestamp: String,
    isRead: Boolean,
    category: RecruitmentNotificationCategory,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val density = LocalDensity.current
    val state = remember { AnchoredDraggableState(initialValue = SwipeAnchor.Closed) }
    var itemSize by remember { mutableStateOf(IntSize.Zero) }
    val isOpen = state.settledValue != SwipeAnchor.Closed

    LaunchedEffect(itemSize, isOpen) {
        if (itemSize == IntSize.Zero) return@LaunchedEffect
        state.updateAnchors(
            DraggableAnchors {
                SwipeAnchor.Closed at 0f
                SwipeAnchor.Open at -itemSize.height.toFloat()
                if (isOpen) SwipeAnchor.Dismissed at -itemSize.width.toFloat()
            }
        )
    }

    LaunchedEffect(state) {
        snapshotFlow { state.settledValue }.collect {
            if (it == SwipeAnchor.Dismissed) onDelete()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth()
                .onSizeChanged { if (itemSize != it) itemSize = it }
                .offset { IntOffset(x = state.offset.takeIf { !it.isNaN() }?.roundToInt() ?: 0, y = 0) }
                .anchoredDraggable(state = state, orientation = Orientation.Horizontal)
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(category.drawableRes),
                contentDescription = stringResource(category.labelRes),
                modifier = Modifier.size(30.dp),
                tint = if (isRead) Color(0xFF727272) else Color(0xFFB611F5)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = KoinTheme.typography.bold13,
                        color = if (isRead) Color(0xFF727272) else Color(0xFF000000),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = timestamp,
                        style = KoinTheme.typography.regular10,
                        color = Color(0xFFCACACA)
                    )
                }
                Text(
                    text = content,
                    style = KoinTheme.typography.medium12,
                    color = Color(0xFF727272),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Box(
            modifier = Modifier
                .width(with(density) { itemSize.height.toDp() })
                .height(with(density) { itemSize.height.toDp() })
                .background(KoinTheme.colors.danger600)
                .clickable(onClick = onDelete)
                .zIndex(-1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_trash_can),
                contentDescription = stringResource(R.string.recruitment_notification_delete_content_description),
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentNotificationItemUnreadPreview() {
    RebrandKoinTheme {
        RecruitmentNotificationItem(
            category = RecruitmentNotificationCategory.MESSAGE,
            title = "팀원모집 @@@님의 메세지",
            content = "메세지메세지",
            timestamp = "2시간 전",
            isRead = false,
            onDelete = {},
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentNotificationItemReadPreview() {
    RebrandKoinTheme {
        RecruitmentNotificationItem(
            category = RecruitmentNotificationCategory.APPLICATION_REJECTED,
            title = "팀원 모집 지원 거절",
            content = "지원했던 AI 공모전 팀원 모집에 승인 거절되었어요.\n다른 모집글에 지원해보세요.",
            timestamp = "2시간 전",
            isRead = true,
            onDelete = {},
            onClick = {}
        )
    }
}
