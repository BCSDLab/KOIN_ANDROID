package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus

@Composable
fun applicantStatusColor(status: ApplicantStatus): Color = when (status) {
    ApplicantStatus.PENDING -> RebrandKoinTheme.colors.neutral500
    ApplicantStatus.APPROVED -> RebrandKoinTheme.colors.primary600
    ApplicantStatus.REJECTED -> RebrandKoinTheme.colors.danger700
}

@Composable
fun ApplicantStatusText(
    status: ApplicantStatus,
    modifier: Modifier = Modifier
) {
    Text(
        text = status.label,
        style = RebrandKoinTheme.typography.medium12,
        color = applicantStatusColor(status),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ApplicantStatusTextPreview() {
    RebrandKoinTheme {
        ApplicantStatusText(status = ApplicantStatus.APPROVED)
    }
}
