package `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.model.Applicant
import `in`.koreatech.koin.feature.recruitment.ui.component.ApplicantAvatar
import `in`.koreatech.koin.feature.recruitment.ui.component.ApplicantStatusText

@Composable
fun ApplicantListItem(
    applicant: Applicant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RebrandKoinTheme.colors.neutral0)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ApplicantAvatar()
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = applicant.name,
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Text(
                    text = "·",
                    style = RebrandKoinTheme.typography.medium12,
                    color = RebrandKoinTheme.colors.neutral500
                )
                ApplicantStatusText(status = applicant.status)
                if (applicant.hasChatRoom) {
                    Icon(
                        painter = painterResource(R.drawable.ic_recruitment_chat),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.primary500,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Text(
                text = stringResource(R.string.recruitment_applicant_role_info, applicant.role, applicant.department, applicant.studentNumber),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_recruitment_chevron_right),
            contentDescription = null,
            tint = RebrandKoinTheme.colors.neutral400,
            modifier = Modifier.size(width = 9.dp, height = 16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun ApplicantListItemPendingPreview() {
    RebrandKoinTheme {
        ApplicantListItem(
            applicant = Applicant(
                id = 1L,
                name = "김철수",
                role = "프론트엔드",
                department = "컴퓨터공학부",
                studentNumber = "23학번",
                status = ApplicantStatus.PENDING
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun ApplicantListItemApprovedPreview() {
    RebrandKoinTheme {
        ApplicantListItem(
            applicant = Applicant(
                id = 2L,
                name = "김철수",
                role = "디자인",
                department = "컴퓨터공학부",
                studentNumber = "23학번",
                status = ApplicantStatus.APPROVED,
                hasChatRoom = true
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun ApplicantListItemRejectedPreview() {
    RebrandKoinTheme {
        ApplicantListItem(
            applicant = Applicant(
                id = 3L,
                name = "김철수",
                role = "백엔드",
                department = "컴퓨터공학부",
                studentNumber = "23학번",
                status = ApplicantStatus.REJECTED
            ),
            onClick = {}
        )
    }
}
