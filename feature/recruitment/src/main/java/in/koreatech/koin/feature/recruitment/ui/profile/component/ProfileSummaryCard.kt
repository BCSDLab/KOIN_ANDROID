package `in`.koreatech.koin.feature.recruitment.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProfile
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentOutlinedActionButton

private val CardShape = RoundedCornerShape(16.dp)
private val AvatarShape = CircleShape

@Composable
fun ProfileSummaryCard(
    profile: RecruitmentProfile,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(RebrandKoinTheme.colors.neutral0, CardShape)
            .padding(16.dp, 20.dp, 16.dp, 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .border(0.5.dp, RebrandKoinTheme.colors.neutral400, AvatarShape)
                    .background(RebrandKoinTheme.colors.neutral0, AvatarShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_profile_avatar),
                    contentDescription = null,
                    tint = RebrandKoinTheme.colors.primary500,
                    modifier = Modifier.size(30.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = profile.nickname,
                    style = RebrandKoinTheme.typography.medium18,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "· ${profile.department}",
                        style = RebrandKoinTheme.typography.regular14,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                    Text(
                        text = "· ${profile.studentId}",
                        style = RebrandKoinTheme.typography.regular14,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            }
        }
        RecruitmentOutlinedActionButton(
            text = stringResource(R.string.recruitment_profile_edit_button),
            onClick = onEditClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSummaryCardPreview() {
    RebrandKoinTheme {
        ProfileSummaryCard(
            profile = RecruitmentProfile(
                nickname = "BCSD",
                department = "컴퓨터공학부",
                studentId = "2023100000"
            ),
            onEditClick = {}
        )
    }
}
