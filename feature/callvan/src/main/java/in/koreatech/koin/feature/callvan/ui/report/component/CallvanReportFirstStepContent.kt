package `in`.koreatech.koin.feature.callvan.ui.report.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason

@Composable
internal fun CallvanReportFirstStepContent(
    selectedReason: CallvanReportReason?,
    onSelectedReasonChange: (CallvanReportReason) -> Unit,
    otherReason: String,
    onOtherReasonChange: (String) -> Unit,
    isOtherReasonError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val callvanReportReasonList = remember {
        listOf(
            CallvanReportReason.NO_SHOW,
            CallvanReportReason.NON_PAYMENT,
            CallvanReportReason.PROFANITY,
            CallvanReportReason.OTHER
        )
    }

    Column(modifier = modifier) {
        CallvanReportHeader()
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items = callvanReportReasonList, key = { reason -> reason.name }) { reason ->
                if (reason == CallvanReportReason.OTHER) {
                    CallvanReportReasonTextFieldItem(
                        reason = reason,
                        isSelected = selectedReason == reason,
                        value = otherReason,
                        onValueChange = onOtherReasonChange,
                        onClick = { onSelectedReasonChange(reason) },
                        isError = isOtherReasonError
                    )
                } else {
                    CallvanReportReasonItem(
                        reason = reason,
                        isSelected = selectedReason == reason,
                        onClick = { onSelectedReasonChange(reason) }
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = KoinTheme.colors.neutral200
                    )
                }
            }
        }
    }
}

@Composable
internal fun CallvanReportHeader() {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.callvan_report_header_title),
            style = KoinTheme.typography.bold18.copy(fontWeight = FontWeight.SemiBold),
            color = KoinTheme.colors.neutral800
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.callvan_report_header_description),
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportFirstStepContentPreview() {
    CallvanReportFirstStepContent(
        selectedReason = null,
        onSelectedReasonChange = {},
        otherReason = "",
        onOtherReasonChange = {},
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    )
}
