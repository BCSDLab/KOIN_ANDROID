package `in`.koreatech.koin.feature.callvan.ui.report

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportFirstStepUiAction
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportFirstStepUiState
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportSecondStepUiAction
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportSecondStepUiState

@Composable
fun CallvanReportScreen(
    onTopbarBackClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    CallvanReportScreenImpl(
        onTopbarBackClick = onTopbarBackClick,
        onSubmitClick = onSubmitClick
    )
}

@Composable
private fun CallvanReportScreenImpl(
    firstStepState: CallvanReportFirstStepUiState = CallvanReportFirstStepUiState(),
    firstStepAction: CallvanReportFirstStepUiAction = CallvanReportFirstStepUiAction(),
    secondStepState: CallvanReportSecondStepUiState = CallvanReportSecondStepUiState(),
    secondStepAction: CallvanReportSecondStepUiAction = CallvanReportSecondStepUiAction(),
    initStep: Int = 1,
    onTopbarBackClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    var step by remember { mutableIntStateOf(initStep) }

    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.callvan_report_top_bar),
                onNavigationIconClick = onTopbarBackClick
            )
        },
        bottomBar = {
            FilledButton(
                text = if (step == 1) {
                    stringResource(R.string.callvan_report_next)
                } else {
                    stringResource(R.string.callvan_detail_participant_report)
                },
                onClick = if (step == 1) {
                    { step = 2 }
                } else {
                    onSubmitClick
                },
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
        when (step) {
            1 -> CallvanReportFirstStepContent(
                selectedReason = firstStepState.selectedReason,
                onSelectedReasonChange = firstStepAction.onSelectedReasonChange,
                otherReason = firstStepState.otherReason,
                onOtherReasonChange = firstStepAction.onOtherReasonChange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .imePadding()
            )
            2 -> CallvanReportSecondStepContent(
                detail = secondStepState.detail,
                onDetailChange = secondStepAction.onDetailChange,
                images = secondStepState.images,
                onAddImageClick = secondStepAction.onAddImageClick,
                onRemoveImage = secondStepAction.onRemoveImage,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .imePadding()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportScreenFirstPreview() {
    CallvanReportScreenImpl(
        initStep = 1
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportScreenSecondPreview() {
    CallvanReportScreenImpl(
        initStep = 2
    )
}
