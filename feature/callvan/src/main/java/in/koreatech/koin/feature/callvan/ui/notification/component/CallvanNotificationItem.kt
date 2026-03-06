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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.notification.model.CallvanNotificationUiItem

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
                text = stringResource(notification.titleRes),
                style = titleStyle,
                color = titleColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.routeInfo,
                    style = KoinTheme.typography.regular12,
                    color = routeColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_participants),
                    contentDescription = null,
                    tint = routeColor
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = stringResource(
                        id = R.string.callvan_notification_participants_form,
                        notification.currentParticipants,
                        notification.maxParticipants
                    ),
                    style = KoinTheme.typography.regular12,
                    color = routeColor
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = notification.message,
                style = KoinTheme.typography.regular14,
                color = titleColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanNotificationItemPreview() {
    CallvanNotificationItem(
        notification = CallvanNotificationUiItem(
            id = 1,
            titleRes = R.string.callvan_notification_title_recruitment_complete,
            routeInfo = "02.02(월) 16:00 인경관 - 천안터미널",
            maxParticipants = 8,
            currentParticipants = 8,
            message = "해당 콜밴팟 인원이 모두 모집되었어요. 콜밴을 예약할까요?",
            isRead = false
        )
    )
}