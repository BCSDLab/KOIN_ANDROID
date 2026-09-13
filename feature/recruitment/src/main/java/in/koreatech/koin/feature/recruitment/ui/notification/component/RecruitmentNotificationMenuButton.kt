package `in`.koreatech.koin.feature.recruitment.ui.notification.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
internal fun RecruitmentNotificationMenuButton(
    onMarkAllAsRead: () -> Unit,
    onDeleteAll: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Icon(
        imageVector = Icons.Default.MoreVert,
        contentDescription = null,
        modifier = Modifier
            .size(24.dp)
            .noRippleClickable { expanded = true }
    )
    DropdownMenu(
        expanded = expanded,
        containerColor = Color(0xFFFAFAFA),
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.recruitment_notification_mark_all_read),
                    style = RebrandKoinTheme.typography.regular13
                )
            },
            onClick = {
                onMarkAllAsRead()
                expanded = false
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.recruitment_notification_delete_all),
                    style = RebrandKoinTheme.typography.regular13,
                    color = Color(0xFFEC2D30)
                )
            },
            onClick = {
                onDeleteAll()
                expanded = false
            }
        )
    }
}
