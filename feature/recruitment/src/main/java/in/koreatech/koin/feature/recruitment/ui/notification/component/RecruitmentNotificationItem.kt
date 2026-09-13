package `in`.koreatech.koin.feature.recruitment.ui.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.notification.model.RecruitmentNotificationCategory

@Composable
internal fun RecruitmentNotificationItem(
    senderNickname: String?,
    content: String,
    timestamp: String,
    isRead: Boolean,
    category: RecruitmentNotificationCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val title = when (category) {
        RecruitmentNotificationCategory.MESSAGE -> senderNickname?.let {
            stringResource(R.string.recruitment_notification_message_with_sender, it)
        } ?: stringResource(category.labelRes)
        else -> stringResource(category.labelRes)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentNotificationItemUnreadPreview() {
    RebrandKoinTheme {
        RecruitmentNotificationItem(
            category = RecruitmentNotificationCategory.MESSAGE,
            senderNickname = "@@@",
            content = "메세지메세지",
            timestamp = "2시간 전",
            isRead = false,
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
            senderNickname = null,
            content = "지원했던 AI 공모전 팀원 모집에 승인 거절되었어요.\n다른 모집글에 지원해보세요.",
            timestamp = "2시간 전",
            isRead = true,
            onClick = {}
        )
    }
}
