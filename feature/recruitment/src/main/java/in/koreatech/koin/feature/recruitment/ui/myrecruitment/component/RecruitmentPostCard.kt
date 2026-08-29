package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.MyRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentStatus
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecruitmentPostCard(
    post: MyRecruitmentPost,
    onApplicantManage: () -> Unit,
    onCloseRecruitment: (() -> Unit)?,
    onChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isComplete = post.status is RecruitmentStatus.Complete

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
                CategoryBadge(category = post.category)
                Spacer(modifier = Modifier.width(4.dp))
                StatusLabel(status = post.status)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onChat,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_chat),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.primary500,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            val applicantColor = if (isComplete) {
                RebrandKoinTheme.colors.primary600
            } else {
                RebrandKoinTheme.colors.neutral500
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                            post.roles.fastForEach { role ->
                                key(role.name) {
                                    RoleChip(role = role)
                                }
                            }
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_location),
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
                                imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_calendar),
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
                                imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_user_group),
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = applicantColor
                            )
                        },
                        text = stringResource(R.string.recruitment_applicant_count, post.currentApplicants, post.maxApplicants),
                        textColor = applicantColor
                    )
                }
            }

            val buttonShape = RoundedCornerShape(16.dp)
            val buttonColors = ButtonColors(
                containerColor = RebrandKoinTheme.colors.neutral0,
                contentColor = RebrandKoinTheme.colors.primary500,
                disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                disabledContentColor = RebrandKoinTheme.colors.neutral500
            )
            val buttonBorder = BorderStroke(0.5.dp, RebrandKoinTheme.colors.primary500)

            if (onCloseRecruitment != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    OutlinedBoxButton(
                        text = stringResource(R.string.recruitment_button_applicant_manage),
                        onClick = onApplicantManage,
                        textStyle = RebrandKoinTheme.typography.regular14,
                        shape = buttonShape,
                        colors = buttonColors,
                        border = buttonBorder,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    )
                    OutlinedBoxButton(
                        text = stringResource(R.string.recruitment_button_close_recruitment),
                        onClick = onCloseRecruitment,
                        textStyle = RebrandKoinTheme.typography.regular14,
                        shape = buttonShape,
                        colors = buttonColors,
                        border = buttonBorder,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    )
                }
            } else {
                OutlinedBoxButton(
                    text = stringResource(R.string.recruitment_button_applicant_manage),
                    onClick = onApplicantManage,
                    textStyle = RebrandKoinTheme.typography.regular14,
                    shape = buttonShape,
                    colors = buttonColors,
                    border = buttonBorder,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryBadge(
    category: RecruitmentCategory,
    modifier: Modifier = Modifier
) {
    val colors = RebrandKoinTheme.colors
    val (bgColor, textColor) = remember(category, colors) {
        when (category) {
            RecruitmentCategory.CONTEST -> colors.info200 to colors.info700
            RecruitmentCategory.EXTERNAL_ACTIVITY -> colors.success200 to colors.success700
            RecruitmentCategory.STUDY -> colors.primary100 to colors.primary600
            RecruitmentCategory.PROJECT -> colors.primary100 to colors.primary600 // TODO: 색상 미정 - 스터디 색상 임시 적용
            RecruitmentCategory.ETC -> colors.neutral200 to colors.neutral600 // TODO: 색상 미정 - neutral 임시 적용
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
private fun StatusLabel(
    status: RecruitmentStatus,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = RebrandKoinTheme.colors
    val (text, color) = remember(status, colors) {
        when (status) {
            is RecruitmentStatus.Recruiting ->
                context.getString(R.string.recruitment_status_d_day, status.daysLeft) to colors.danger700
            RecruitmentStatus.Complete ->
                context.getString(R.string.recruitment_status_complete) to colors.primary600
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
            .padding(horizontal = 8.dp, vertical = 2.dp)
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
    modifier: Modifier = Modifier,
    textColor: Color = RebrandKoinTheme.colors.neutral500
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
            color = textColor
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun RecruitmentPostCardRecruitingPreview() {
    RebrandKoinTheme {
        RecruitmentPostCard(
            post = MyRecruitmentPost(
                id = 1L,
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
            onApplicantManage = {},
            onCloseRecruitment = {},
            onChat = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun RecruitmentPostCardCompletePreview() {
    RebrandKoinTheme {
        RecruitmentPostCard(
            post = MyRecruitmentPost(
                id = 2L,
                category = RecruitmentCategory.STUDY,
                status = RecruitmentStatus.Complete,
                title = "2026 스터디 팀원 모집",
                location = "온·오프라인",
                dateRange = "2026.07.26 ~ 2026.08.07",
                currentApplicants = 3,
                maxApplicants = 3
            ),
            onApplicantManage = {},
            onCloseRecruitment = null,
            onChat = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun RecruitmentPostCardExternalActivityPreview() {
    RebrandKoinTheme {
        RecruitmentPostCard(
            post = MyRecruitmentPost(
                id = 3L,
                category = RecruitmentCategory.EXTERNAL_ACTIVITY,
                status = RecruitmentStatus.Complete,
                title = "2026 대외활동 팀원 모집",
                location = "온·오프라인",
                dateRange = "2026.07.26 ~ 2026.08.07",
                currentApplicants = 5,
                maxApplicants = 5
            ),
            onApplicantManage = {},
            onCloseRecruitment = null,
            onChat = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
