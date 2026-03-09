package `in`.koreatech.koin.feature.callvan.ui.report.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason

const val CALLVAN_REPORT_OTHER_REASON_MAX_LENGTH = 150

@Composable
fun CallvanReportReasonItem(
    reason: CallvanReportReason,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.padding(start = 8.dp),
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = RebrandKoinTheme.colors.primary500,
                unselectedColor = KoinTheme.colors.neutral500
            )
        )
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(reason.titleRes),
                style = KoinTheme.typography.medium18,
                color = KoinTheme.colors.neutral800
            )
            reason.descriptionRes?.let {
                Text(
                    text = stringResource(it),
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.neutral600
                )
            }
        }
    }
}

@Composable
fun CallvanReportReasonTextFieldItem(
    reason: CallvanReportReason,
    isSelected: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                modifier = Modifier.padding(start = 8.dp),
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = RebrandKoinTheme.colors.primary500,
                    unselectedColor = KoinTheme.colors.neutral500
                )
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(reason.titleRes),
                    style = KoinTheme.typography.medium18,
                    color = KoinTheme.colors.neutral800
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(
                        R.string.callvan_report_other_reason_length,
                        value.length,
                        CALLVAN_REPORT_OTHER_REASON_MAX_LENGTH
                    ),
                    style = KoinTheme.typography.regular12,
                    color = if (value.length >= CALLVAN_REPORT_OTHER_REASON_MAX_LENGTH) {
                        RebrandKoinTheme.colors.primary500
                    } else {
                        KoinTheme.colors.neutral500
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        CallvanReportTextField(
            modifier = Modifier.padding(horizontal = 8.dp),
            value = value,
            onValueChange = onValueChange,
            readOnly = !isSelected,
            maxLength = CALLVAN_REPORT_OTHER_REASON_MAX_LENGTH,
            placeholder = stringResource(R.string.callvan_report_other_placeholder)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportReasonItemPreview() {
    CallvanReportReasonItem(
        reason = CallvanReportReason.NO_SHOW,
        isSelected = false,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportReasonItemSelectedPreview() {
    CallvanReportReasonItem(
        reason = CallvanReportReason.NO_SHOW,
        isSelected = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportReasonTextFieldItemPreview() {
    CallvanReportReasonTextFieldItem(
        reason = CallvanReportReason.OTHER,
        isSelected = false,
        value = "",
        onValueChange = {},
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportReasonTextFieldItemSelectedPreview() {
    CallvanReportReasonTextFieldItem(
        reason = CallvanReportReason.OTHER,
        isSelected = true,
        value = "신고 사유 입력 예시",
        onValueChange = {},
        onClick = {}
    )
}
