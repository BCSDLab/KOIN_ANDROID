package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanNotificationIcon(
    hasNewNotification: Boolean = false
) {
    Box {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_callvan_notification),
            contentDescription = null
        )
        if (hasNewNotification) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = RebrandKoinTheme.colors.primary500,
                        shape = CircleShape
                    )
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@Preview
@Composable
private fun CallvanNotificationIconPreview() {
    CallvanNotificationIcon(hasNewNotification = false)
}

@Preview
@Composable
private fun CallvanNotificationIconHasNewNotificationPreview() {
    CallvanNotificationIcon(hasNewNotification = true)
}
