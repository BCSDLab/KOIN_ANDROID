package `in`.koreatech.koin.feature.callvan.ui.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

data class CallvanNotificationUiItem(
    val id: Int,
    val title: String,
    val routeInfo: String,
    val message: String,
    val isRead: Boolean
)

@Composable
fun CallvanNotificationItem(
    notification: CallvanNotificationUiItem,
    modifier: Modifier = Modifier
) {
    val titleColor = if (notification.isRead) KoinTheme.colors.neutral500 else KoinTheme.colors.neutral800
    val titleStyle = if (notification.isRead) KoinTheme.typography.regular14 else KoinTheme.typography.medium14
    val routeColor = if (notification.isRead) KoinTheme.colors.neutral500 else RebrandKoinTheme.colors.primary500

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    if (notification.isRead) {
                        KoinTheme.colors.neutral0
                    } else {
                        RebrandKoinTheme.colors.primary500
                    }
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = notification.title,
                style = titleStyle,
                color = titleColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = notification.routeInfo,
                style = KoinTheme.typography.regular12,
                color = routeColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = notification.message,
                style = KoinTheme.typography.regular14,
                color = titleColor
            )
        }
    }
}
