package `in`.koreatech.koin.feature.callvan.ui.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.report.component.CallvanReportReasonTextFieldItem
import `in`.koreatech.koin.feature.callvan.ui.report.component.CallvanReportReasonItem
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason

@Composable
fun CallvanReportScreen(
    onTopbarBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    CallvanReportScreenImpl(
        onTopbarBackClick = onTopbarBackClick,
        onNextClick = onNextClick
    )
}

@Composable
private fun CallvanReportScreenImpl(
    selectedReason: CallvanReportReason? = null,
    onSelectedReasonChange: (CallvanReportReason) -> Unit = {},
    otherReason: String = "",
    onOtherReasonChange: (String) -> Unit = {},
    onTopbarBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    val callvanReportReasonList = remember {
        listOf(
            CallvanReportReason.NO_SHOW,
            CallvanReportReason.NON_PAYMENT,
            CallvanReportReason.PROFANITY,
            CallvanReportReason.OTHER
        )
    }

    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.callvan_report_top_bar),
                onNavigationIconClick = onTopbarBackClick
            )
        },
        bottomBar = {
            FilledButton(
                text = stringResource(R.string.callvan_report_next),
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500
                ),
                shape = KoinTheme.shapes.small
            )
        },
        containerColor = KoinTheme.colors.neutral0
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .imePadding()
        ) {
            CallvanReportHeader()
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(callvanReportReasonList) { reason ->
                    if (reason == CallvanReportReason.OTHER) {
                        CallvanReportReasonTextFieldItem(
                            reason = reason,
                            isSelected = selectedReason == reason,
                            value = otherReason,
                            onValueChange = onOtherReasonChange,
                            onClick = { onSelectedReasonChange(reason) }
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
}

@Composable
private fun CallvanReportHeader() {
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
            color = Color(0xFF8E8E8E)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportScreenPreview() {
    CallvanReportScreenImpl()
}