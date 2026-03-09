package `in`.koreatech.koin.feature.chat.ui.groupchat.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.groupchat.GroupChatPreviewData

object GroupChatTopBarDefaults {
    @Composable
    fun purpleColors(
        containerColor: Color = RebrandKoinTheme.colors.neutral0,
        titleColor: Color = RebrandKoinTheme.colors.neutral800,
        subtitleColor: Color = RebrandKoinTheme.colors.neutral600,
        iconColor: Color = RebrandKoinTheme.colors.neutral800,
        memberCountColor: Color = RebrandKoinTheme.colors.neutral600,
        menuBackgroundColor: Color = RebrandKoinTheme.colors.neutral50
    ): GroupChatTopBarColors = GroupChatTopBarColors(
        containerColor = containerColor,
        titleColor = titleColor,
        subtitleColor = subtitleColor,
        iconColor = iconColor,
        memberCountColor = memberCountColor,
        menuBackgroundColor = menuBackgroundColor
    )
}

@Immutable
data class GroupChatTopBarColors(
    val containerColor: Color,
    val titleColor: Color,
    val subtitleColor: Color,
    val iconColor: Color,
    val memberCountColor: Color,
    val menuBackgroundColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatTopBar(
    departure: String,
    arrival: String,
    departureTime: String,
    currentMemberCount: Int,
    maxMemberCount: Int,
    modifier: Modifier = Modifier,
    colors: GroupChatTopBarColors = GroupChatTopBarDefaults.purpleColors(),
    onNavigationIconClick: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.containerColor
        ),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.chat_back),
                    tint = colors.iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = departure,
                    style = RebrandKoinTheme.typography.medium15,
                    color = colors.titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "-",
                    style = RebrandKoinTheme.typography.medium15,
                    color = colors.titleColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = arrival,
                    style = RebrandKoinTheme.typography.medium15,
                    color = colors.titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = departureTime,
                    style = RebrandKoinTheme.typography.medium15,
                    color = colors.titleColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_chat_user_count),
                        contentDescription = stringResource(id = R.string.group_chat_member_count_icon),
                        modifier = Modifier.size(16.dp),
                        tint = colors.memberCountColor
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = stringResource(R.string.group_chat_toolbar_participants, currentMemberCount, maxMemberCount),
                        style = RebrandKoinTheme.typography.regular12,
                        color = colors.memberCountColor
                    )
                }
            }
        },
        actions = {
            Box(modifier = Modifier.size(24.dp))
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun GroupChatTopBarPreview() {
    KoinSurface {
        Column {
            GroupChatTopBar(
                departure = GroupChatPreviewData.DEPARTURE,
                arrival = GroupChatPreviewData.ARRIVAL,
                departureTime = GroupChatPreviewData.DEPARTURE_TIME,
                currentMemberCount = GroupChatPreviewData.CURRENT_MEMBER_COUNT,
                maxMemberCount = GroupChatPreviewData.MAX_MEMBER_COUNT,
                onNavigationIconClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            GroupChatTopBar(
                departure = GroupChatPreviewData.LONG_DEPARTURE,
                arrival = GroupChatPreviewData.LONG_ARRIVAL,
                departureTime = GroupChatPreviewData.DEPARTURE_TIME,
                currentMemberCount = GroupChatPreviewData.CURRENT_MEMBER_COUNT,
                maxMemberCount = GroupChatPreviewData.MAX_MEMBER_COUNT
            )
        }
    }
}
