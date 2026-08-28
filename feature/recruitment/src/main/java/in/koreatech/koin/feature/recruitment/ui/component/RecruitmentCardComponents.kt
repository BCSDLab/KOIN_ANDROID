package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
internal fun RecruitmentCategoryBadge(
    category: RecruitmentCategory,
    modifier: Modifier = Modifier
) {
    val colors = RebrandKoinTheme.colors
    val (bgColor, textColor) = remember(category, colors) {
        when (category) {
            RecruitmentCategory.CONTEST -> colors.info200 to colors.info700
            RecruitmentCategory.EXTERNAL_ACTIVITY -> colors.success200 to colors.success700
            RecruitmentCategory.STUDY -> colors.primary100 to colors.primary600
            RecruitmentCategory.PROJECT -> colors.primary100 to colors.primary600
            RecruitmentCategory.ETC -> colors.neutral200 to colors.neutral600
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
internal fun RecruitmentRoleChip(
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
internal fun RecruitmentInfoItem(
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

@Preview(showBackground = false)
@Composable
private fun RecruitmentCategoryBadgePreview() {
    RebrandKoinTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RecruitmentCategory.entries.forEach { category ->
                RecruitmentCategoryBadge(category = category)
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun RecruitmentRoleChipPreview() {
    RebrandKoinTheme {
        RecruitmentRoleChip(
            role = RecruitmentRole("프론트엔드", 2),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun RecruitmentInfoItemPreview() {
    RebrandKoinTheme {
        RecruitmentInfoItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_location),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = RebrandKoinTheme.colors.neutral500
                )
            },
            text = "온라인",
            modifier = Modifier.padding(16.dp)
        )
    }
}
