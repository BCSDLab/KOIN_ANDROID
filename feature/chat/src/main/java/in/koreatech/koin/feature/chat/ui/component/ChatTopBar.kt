package `in`.koreatech.koin.feature.chat.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    memberCount: Int? = null,
    maxMemberCount: Int? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    KoinTopAppBar2(
        title = {
            Text(
                text = title,
                style = RebrandKoinTheme.typography.bold16,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        onNavigationIconClick = onNavigationIconClick,
        actions = {
            if (memberCount != null) {
                Icon(
                    painter = painterResource(R.drawable.ic_chat_person_count),
                    contentDescription = stringResource(R.string.chat_member_count_icon),
                    modifier = Modifier.size(16.dp),
                    tint = RebrandKoinTheme.colors.neutral600
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = if (maxMemberCount == null) {
                        memberCount.toString()
                    } else {
                        stringResource(R.string.chat_toolbar_participants, memberCount, maxMemberCount)
                    },
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral600
                )
            }
            actions()
        }
    )
}
