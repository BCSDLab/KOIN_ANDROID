package `in`.koreatech.koin.feature.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.card.FeatureRow
import `in`.koreatech.koin.core.designsystem.component.card.FeatureRowDefaults
import `in`.koreatech.koin.core.designsystem.component.icon.IconBadge
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.profile.R

@Composable
fun ProfileCard(
    isLoggedIn: Boolean,
    name: String,
    studentNumber: String,
    modifier: Modifier = Modifier,
    onAuthClick: () -> Unit = {},
    onSettingClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(RebrandKoinTheme.colors.neutral0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            ProfileAvatar()
            Column {
                Text(
                    text = if (isLoggedIn) {
                        name.ifBlank { stringResource(R.string.profile_username_default) }
                    } else {
                        stringResource(R.string.profile_login_prompt)
                    },
                    style = RebrandKoinTheme.typography.bold16,
                    color = RebrandKoinTheme.colors.neutral800
                )
                if (isLoggedIn && studentNumber.isNotBlank()) {
                    Text(
                        text = studentNumber,
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            }
        }

        ProfileMenuRow(
            icon = ImageVector.vectorResource(
                if (isLoggedIn) R.drawable.ic_profile_logout else R.drawable.ic_profile_login
            ),
            label = stringResource(
                if (isLoggedIn) R.string.profile_logout else R.string.profile_login
            ),
            onClick = onAuthClick
        )

        ProfileMenuRow(
            icon = ImageVector.vectorResource(R.drawable.ic_profile_setting),
            label = stringResource(R.string.profile_setting),
            onClick = onSettingClick
        )
    }
}

@Composable
private fun ProfileAvatar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(0.5.dp, RebrandKoinTheme.colors.neutral300, CircleShape)
            .background(RebrandKoinTheme.colors.neutral0)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_profile_avatar),
            tint = RebrandKoinTheme.colors.primary500,
            contentDescription = null
        )
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    FeatureRow(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        title = label,
        titleStyle = RebrandKoinTheme.typography.bold15.copy(color = RebrandKoinTheme.colors.neutral800),
        icon = {
            IconBadge(
                imageVector = icon,
                contentDescription = null,
                shape = RoundedCornerShape(10.dp)
            )
        },
        colors = FeatureRowDefaults.colors(
            backgroundColor = RebrandKoinTheme.colors.neutral0
        ),
        shape = RectangleShape,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        borderWidth = 0.dp,
        iconSpacing = 16.dp
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileCardLoggedInPreview() {
    ProfileCard(
        isLoggedIn = true,
        name = "BCSD",
        studentNumber = "20231020329"
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileCardLoggedOutPreview() {
    ProfileCard(
        isLoggedIn = false,
        name = "",
        studentNumber = ""
    )
}
