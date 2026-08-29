package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory

@Composable
fun RecruitmentCategoryBadge(
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

@Preview(showBackground = true)
@Composable
private fun RecruitmentCategoryBadgePreview() {
    RebrandKoinTheme {
        RecruitmentCategoryBadge(category = RecruitmentCategory.CONTEST)
    }
}
