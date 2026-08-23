package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedRecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppliedRecruitmentPostCard(
    post: AppliedRecruitmentPost,
    modifier: Modifier = Modifier
) {
    val isApproved = post.applicationStatus is AppliedRecruitmentStatus.Approved

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
                    CategoryBadge(category = post.category)
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
                        post.roles.forEach { RoleChip(role = it) }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_recruitment_location),
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = RebrandKoinTheme.colors.neutral500
                            )
                        },
                        text = post.location
                    )
                    InfoItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_recruitment_calendar),
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = RebrandKoinTheme.colors.neutral500
                            )
                        },
                        text = post.dateRange
                    )
                    InfoItem(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_recruitment_user_group),
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = RebrandKoinTheme.colors.neutral500
                            )
                        },
                        text = stringResource(R.string.recruitment_applicant_count, post.currentApplicants, post.maxApplicants)
                    )
                }
                if (isApproved) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.ic_recruitment_chat),
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
private fun CategoryBadge(
    category: RecruitmentCategory,
    modifier: Modifier = Modifier
) {
    val bgColor: Color
    val textColor: Color
    when (category) {
        RecruitmentCategory.CONTEST -> {
            bgColor = RebrandKoinTheme.colors.info200
            textColor = RebrandKoinTheme.colors.info700
        }
        RecruitmentCategory.EXTERNAL_ACTIVITY -> {
            bgColor = RebrandKoinTheme.colors.success200
            textColor = RebrandKoinTheme.colors.success700
        }
        RecruitmentCategory.STUDY -> {
            bgColor = RebrandKoinTheme.colors.primary100
            textColor = RebrandKoinTheme.colors.primary600
        }
        RecruitmentCategory.PROJECT -> {
            // TODO: 색상 미정 - 스터디 색상 임시 적용
            bgColor = RebrandKoinTheme.colors.primary100
            textColor = RebrandKoinTheme.colors.primary600
        }
        RecruitmentCategory.ETC -> {
            // TODO: 색상 미정 - neutral 임시 적용
            bgColor = RebrandKoinTheme.colors.neutral200
            textColor = RebrandKoinTheme.colors.neutral600
        }
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 1.dp)
    ) {
        Text(
            text = category.label,
            style = RebrandKoinTheme.typography.regular10.copy(fontWeight = FontWeight.Medium),
            color = textColor
        )
    }
}

@Composable
private fun ApplicationStatusLabel(
    status: AppliedRecruitmentStatus,
    modifier: Modifier = Modifier
) {
    val text: String
    val color: Color
    when (status) {
        AppliedRecruitmentStatus.Approved -> {
            text = stringResource(R.string.recruitment_applied_status_approved)
            color = RebrandKoinTheme.colors.primary600
        }
        AppliedRecruitmentStatus.Pending -> {
            text = stringResource(R.string.recruitment_applied_status_pending)
            color = RebrandKoinTheme.colors.neutral500
        }
        AppliedRecruitmentStatus.Rejected -> {
            text = stringResource(R.string.recruitment_applied_status_rejected)
            color = RebrandKoinTheme.colors.danger700
        }
    }
    Text(
        text = text,
        style = RebrandKoinTheme.typography.regular10.copy(fontWeight = FontWeight.Medium),
        color = color,
        modifier = modifier
    )
}

@Composable
private fun RoleChip(
    role: RecruitmentRole,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(RebrandKoinTheme.colors.neutral200)
            .padding(horizontal = 8.dp, vertical = 1.dp)
    ) {
        Text(
            text = stringResource(R.string.recruitment_role_item, role.name, role.count),
            style = RebrandKoinTheme.typography.regular10,
            color = RebrandKoinTheme.colors.neutral500
        )
    }
}

@Composable
private fun InfoItem(
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        icon()
        Text(
            text = text,
            style = RebrandKoinTheme.typography.regular10,
            color = RebrandKoinTheme.colors.neutral500
        )
    }
}

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
                roles = listOf(
                    RecruitmentRole("프론트엔드", 1),
                    RecruitmentRole("백엔드", 1)
                ),
                location = "온라인",
                dateRange = "2026.07.26 ~ 2026.08.07",
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
                dateRange = "2026.07.26 ~ 2026.08.07",
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
                dateRange = "2026.07.26 ~ 2026.08.07",
                currentApplicants = 5,
                maxApplicants = 5
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
