package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentCategoryBadge
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentPostMetaInfo
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentRoleChip
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedRecruitmentStatus
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppliedRecruitmentPostCard(
    post: AppliedRecruitmentPost,
    modifier: Modifier = Modifier
) {
    val isApproved = remember(post.applicationStatus) { post.applicationStatus is AppliedRecruitmentStatus.Approved }

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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RecruitmentCategoryBadge(category = post.category)
                    if (post.daysLeft != null) {
                        Text(
                            text = stringResource(R.string.recruitment_status_d_day, post.daysLeft),
                            style = RebrandKoinTheme.typography.regular10.copy(fontWeight = FontWeight.Medium),
                            color = RebrandKoinTheme.colors.danger700
                        )
                    }
                }
                ApplicationStatusLabel(status = post.applicationStatus)
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = post.title,
                    style = RebrandKoinTheme.typography.bold16,
                    color = RebrandKoinTheme.colors.neutral800
                )
                if (post.roles.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        post.roles.fastForEach { role ->
                            key(role.name) {
                                RecruitmentRoleChip(role = role)
                            }
                        }
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
                    applicantText = stringResource(R.string.recruitment_applicant_count, post.currentApplicants, post.maxApplicants)
                )
                if (isApproved) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_chat),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.primary500,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationStatusLabel(
    status: AppliedRecruitmentStatus,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = RebrandKoinTheme.colors
    val (text, color) = remember(status, colors, context) {
        when (status) {
            AppliedRecruitmentStatus.Approved ->
                context.getString(R.string.recruitment_applied_status_approved) to colors.primary600
            AppliedRecruitmentStatus.Pending ->
                context.getString(R.string.recruitment_applied_status_pending) to colors.neutral500
            AppliedRecruitmentStatus.Rejected ->
                context.getString(R.string.recruitment_applied_status_rejected) to colors.danger700
        }
    }
    Text(
        text = text,
        style = RebrandKoinTheme.typography.regular10.copy(fontWeight = FontWeight.Medium),
        color = color,
        modifier = modifier
    )
}

private const val PREVIEW_DATE_RANGE = "2026.07.26 ~ 2026.08.07"

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun AppliedPostCardApprovedPreview() {
    RebrandKoinTheme {
        AppliedRecruitmentPostCard(
            post = AppliedRecruitmentPost(
                id = 1L,
                category = RecruitmentCategory.CONTEST,
                applicationStatus = AppliedRecruitmentStatus.Approved,
                daysLeft = 5,
                title = "AI 아이디어 공모전 팀원 모집",
                roles = persistentListOf(
                    RecruitmentRole("프론트엔드", 1),
                    RecruitmentRole("백엔드", 1)
                ),
                location = "온라인",
                dateRange = PREVIEW_DATE_RANGE,
                currentApplicants = 2,
                maxApplicants = 3
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun AppliedPostCardPendingPreview() {
    RebrandKoinTheme {
        AppliedRecruitmentPostCard(
            post = AppliedRecruitmentPost(
                id = 2L,
                category = RecruitmentCategory.STUDY,
                applicationStatus = AppliedRecruitmentStatus.Pending,
                daysLeft = 3,
                title = "2026 스터디 팀원 모집",
                location = "온·오프라인",
                dateRange = PREVIEW_DATE_RANGE,
                currentApplicants = 1,
                maxApplicants = 5
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun AppliedPostCardRejectedPreview() {
    RebrandKoinTheme {
        AppliedRecruitmentPostCard(
            post = AppliedRecruitmentPost(
                id = 3L,
                category = RecruitmentCategory.EXTERNAL_ACTIVITY,
                applicationStatus = AppliedRecruitmentStatus.Rejected,
                daysLeft = null,
                title = "2026 대외활동 팀원 모집",
                location = "온·오프라인",
                dateRange = PREVIEW_DATE_RANGE,
                currentApplicants = 5,
                maxApplicants = 5
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
