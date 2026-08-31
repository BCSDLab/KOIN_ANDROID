package `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentCategoryBadge
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentPostMetaInfo
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentRoleChip
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.MyRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentStatus
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ApplicantManagementPostCard(
    post: MyRecruitmentPost,
    onChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (recruitStatusText, recruitStatusColor) = when (post.status) {
        is RecruitmentStatus.Recruiting -> {
            stringResource(R.string.recruitment_filter_status_recruiting) to RebrandKoinTheme.colors.primary600
        }
        RecruitmentStatus.Complete -> {
            // TODO: Figma에 마감 상태의 카드 디자인이 없어 텍스트/색상 임시 지정
            stringResource(R.string.recruitment_status_complete) to RebrandKoinTheme.colors.neutral500
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RebrandKoinTheme.colors.neutral0),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RecruitmentCategoryBadge(category = post.category)
                Spacer(modifier = Modifier.width(4.dp))
                if (post.status is RecruitmentStatus.Recruiting) {
                    Text(
                        text = stringResource(R.string.recruitment_status_d_day, post.status.daysLeft),
                        style = RebrandKoinTheme.typography.regular10.copy(fontWeight = FontWeight.Medium),
                        color = RebrandKoinTheme.colors.danger700
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = recruitStatusText,
                    style = RebrandKoinTheme.typography.regular10.copy(fontWeight = FontWeight.Medium),
                    color = recruitStatusColor
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = post.title,
                    style = RebrandKoinTheme.typography.bold16,
                    color = RebrandKoinTheme.colors.neutral700
                )
                if (post.roles.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        post.roles.forEach { RecruitmentRoleChip(role = it) }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RecruitmentPostMetaInfo(
                    location = post.location,
                    dateRange = post.dateRange,
                    applicantText = stringResource(R.string.recruitment_applicant_count, post.currentApplicants, post.maxApplicants),
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onChat,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_chat),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.primary500
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun ApplicantManagementPostCardPreview() {
    RebrandKoinTheme {
        ApplicantManagementPostCard(
            post = MyRecruitmentPost(
                id = 1,
                category = RecruitmentCategory.CONTEST,
                status = RecruitmentStatus.Recruiting(daysLeft = 5),
                title = "AI 아이디어 공모전 팀원 모집",
                roles = persistentListOf(
                    RecruitmentRole("프론트엔드", 1),
                    RecruitmentRole("백엔드", 1),
                    RecruitmentRole("디자인", 1)
                ),
                location = "온라인",
                dateRange = "2026.07.26 ~ 2026.08.07",
                currentApplicants = 0,
                maxApplicants = 3
            ),
            onChat = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
