package `in`.koreatech.koin.feature.notification.component

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
import androidx.compose.foundation.layout.fillMaxSize
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
import `in`.koreatech.koin.feature.notification.R
import `in`.koreatech.koin.feature.notification.model.NotificationCategory
import kotlin.math.roundToInt

private enum class SwipeAnchor { Closed, Open, Dismissed }

@Suppress("CyclomaticComplexMethod")
@Composable
fun NotificationItem(
    title: String,
    content: String,
    timestamp: String,
    isRead: Boolean,
    type: String,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val density = LocalDensity.current
    val state = remember { AnchoredDraggableState(initialValue = SwipeAnchor.Closed) }
    var itemSize by remember { mutableStateOf(IntSize.Zero) }
    val isOpen = state.settledValue != SwipeAnchor.Closed
    val drawableRes = remember(type) { NotificationCategory.from(type).drawableRes }

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
            Box(modifier = Modifier.size(30.dp)) {
                drawableRes?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(it),
                        contentDescription = type,
                        modifier = Modifier.fillMaxSize(),
                        tint = if (isRead) Color(0xFF727272) else Color(0xFFB611F5)
                    )
                }
            }
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
                    maxLines = 1,
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
                contentDescription = stringResource(R.string.notification_delete_content_description),
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationItemUnreadPreview() {
    RebrandKoinTheme {
        NotificationItem(
            type = "callvan",
            title = "콜벤팟 OOO님의 메시지",
            content = "다들 어디신가요?",
            timestamp = "2시간 전",
            isRead = false,
            onDelete = {},
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationItemReadPreview() {
    RebrandKoinTheme {
        NotificationItem(
            type = "callvan",
            title = "콜밴팟 새 참여자",
            content = "OOO님이 콜벤팟에 참여했어요.",
            timestamp = "2시간 전",
            isRead = true,
            onDelete = {},
            onClick = {}
        )
    }
}
