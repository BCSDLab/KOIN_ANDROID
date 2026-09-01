package `in`.koreatech.koin.feature.recruitment.ui.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory

@Composable
fun RecruitmentTitleSection(
    category: RecruitmentCategory,
    dDay: Int,
    isClosed: Boolean,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReadOnlyTextChip(
                title = stringResource(category.labelRes),
                containerColor = RebrandKoinTheme.colors.info200,
                textStyle = RebrandKoinTheme.typography.regular10.copy(
                    fontWeight = FontWeight.Medium,
                    color = RebrandKoinTheme.colors.info700
                ),
                shape = RoundedCornerShape(percent = 50),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 1.dp)
            )
            Text(
                text = dDayText(dDay = dDay, isClosed = isClosed),
                style = RebrandKoinTheme.typography.regular10.copy(
                    fontWeight = FontWeight.Medium,
                    color = RebrandKoinTheme.colors.danger700
                )
            )
        }
        Text(
            text = title,
            style = RebrandKoinTheme.typography.bold18.copy(
                color = RebrandKoinTheme.colors.neutral700
            )
        )
    }
}

@Composable
private fun dDayText(dDay: Int, isClosed: Boolean): String = when {
    isClosed || dDay < 0 -> stringResource(R.string.recruitment_closed)
    dDay == 0 -> stringResource(R.string.recruitment_d_day_today)
    else -> stringResource(R.string.recruitment_d_day, dDay)
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentTitleSectionPreview() {
    RebrandKoinTheme {
        RecruitmentTitleSection(
            category = RecruitmentCategory.CONTEST,
            dDay = 5,
            isClosed = false,
            title = "AI 아이디어 공모전 팀원 모집"
        )
    }
}
