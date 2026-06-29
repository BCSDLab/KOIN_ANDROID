package `in`.koreatech.koin.feature.notification.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import `in`.koreatech.koin.feature.notification.R

@Composable
fun NotificationItemIcon(type: String) {
    val drawableRes = when (type) {
        "shop" -> R.drawable.ic_notification_store
        "dining" -> R.drawable.ic_notification_dining
        "lost-item" -> R.drawable.ic_notification_lostitem
        "chat" -> R.drawable.ic_notification_chat
        "callvan", "callvan-chat" -> R.drawable.ic_notification_callvan
        else -> null
    } ?: return

    Icon(
        imageVector = ImageVector.vectorResource(drawableRes),
        contentDescription = type,
        modifier = Modifier.fillMaxSize()
    )
}
