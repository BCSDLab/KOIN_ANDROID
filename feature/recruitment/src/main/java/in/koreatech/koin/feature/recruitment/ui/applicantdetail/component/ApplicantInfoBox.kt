package `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
fun ApplicantLabeledInfoBox(
    label: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = RebrandKoinTheme.typography.medium14,
            color = RebrandKoinTheme.colors.neutral800
        )
        Text(
            text = content,
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral500,
            modifier = Modifier
                .fillMaxWidth()
                .background(RebrandKoinTheme.colors.neutral100, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ApplicantActivityInfoBox(
    title: String,
    period: String,
    content: String,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.recruitment_applicant_detail_activities),
    periodLabel: String = stringResource(R.string.recruitment_applicant_detail_activity_period),
    contentLabel: String = stringResource(R.string.recruitment_applicant_detail_activity_content)
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = RebrandKoinTheme.typography.medium14,
            color = RebrandKoinTheme.colors.neutral800
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(RebrandKoinTheme.colors.neutral100, RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = RebrandKoinTheme.typography.bold14,
                color = RebrandKoinTheme.colors.neutral800
            )
            ApplicantActivityRow(label = periodLabel, value = period)
            ApplicantActivityRow(label = contentLabel, value = content)
        }
    }
}

@Composable
private fun ApplicantActivityRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral500,
            modifier = Modifier.width(45.dp)
        )
        Text(
            text = value,
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral800,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun ApplicantLabeledInfoBoxPreview() {
    RebrandKoinTheme {
        ApplicantLabeledInfoBox(label = "자기소개", content = "안녕하세요.")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun ApplicantActivityInfoBoxPreview() {
    RebrandKoinTheme {
        ApplicantActivityInfoBox(
            title = "AI 공모전",
            period = "2026.03.23 - 2026.04.06",
            content = "AI 공모전에서 기획을 담당했고 @@@를 주제로 @@@를 만들었습니다"
        )
    }
}
