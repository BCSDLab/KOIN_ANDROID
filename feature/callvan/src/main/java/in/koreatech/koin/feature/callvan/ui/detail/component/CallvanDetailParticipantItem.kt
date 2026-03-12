package `in`.koreatech.koin.feature.callvan.ui.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanDropdownMenu
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanDropdownMenuItem
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanPersonIcon
import `in`.koreatech.koin.feature.callvan.ui.detail.model.CallvanDetailParticipantUiItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CallvanDetailParticipantItem(
    participant: CallvanDetailParticipantUiItem,
    menuItems: ImmutableList<CallvanDropdownMenuItem>,
    modifier: Modifier = Modifier,
    tint: Color = RebrandKoinTheme.colors.primary500
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CallvanPersonIcon(
            modifier = Modifier.size(32.dp),
            isMine = participant.isMe,
            tint = tint
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = if (participant.isMe) {
                stringResource(R.string.callvan_detail_participant_name_me, participant.name)
            } else {
                participant.name
            },
            style = KoinTheme.typography.medium16,
            color = KoinTheme.colors.neutral800,
            modifier = Modifier.weight(1f)
        )
        if (!participant.isMe) {
            Box {
                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = { isMenuExpanded = true }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_menu_small),
                        contentDescription = null,
                        tint = KoinTheme.colors.neutral600
                    )
                }
                CallvanDropdownMenu(
                    expanded = isMenuExpanded,
                    items = menuItems,
                    topPadding = 32.dp,
                    onDismissRequest = { isMenuExpanded = false }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanDetailParticipantItemPreview() {
    CallvanDetailParticipantItem(
        participant = CallvanDetailParticipantUiItem(
            id = 1,
            name = "홍길동",
            isMe = false
        ),
        menuItems = persistentListOf()
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanDetailParticipantItemIsMinePreview() {
    CallvanDetailParticipantItem(
        participant = CallvanDetailParticipantUiItem(
            id = 1,
            name = "홍길동",
            isMe = true
        ),
        menuItems = persistentListOf()
    )
}
