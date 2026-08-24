package `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
fun ApplicantDecisionButtons(
    onReject: () -> Unit,
    onApprove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedBoxButton(
            text = stringResource(R.string.recruitment_applicant_detail_reject),
            onClick = onReject,
            textStyle = RebrandKoinTheme.typography.bold15,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonColors(
                containerColor = RebrandKoinTheme.colors.neutral0,
                contentColor = RebrandKoinTheme.colors.primary500,
                disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                disabledContentColor = RebrandKoinTheme.colors.neutral600
            ),
            border = BorderStroke(0.5.dp, RebrandKoinTheme.colors.primary500),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        )
        FilledButton(
            text = stringResource(R.string.recruitment_applicant_detail_approve),
            onClick = onApprove,
            textStyle = RebrandKoinTheme.typography.bold15,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonColors(
                containerColor = RebrandKoinTheme.colors.primary500,
                contentColor = RebrandKoinTheme.colors.neutral0,
                disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                disabledContentColor = RebrandKoinTheme.colors.neutral600
            ),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ApplicantDecisionButtonsPreview() {
    RebrandKoinTheme {
        ApplicantDecisionButtons(onReject = {}, onApprove = {})
    }
}
