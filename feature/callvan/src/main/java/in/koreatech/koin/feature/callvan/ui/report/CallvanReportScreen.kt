package `in`.koreatech.koin.feature.callvan.ui.report

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.report.component.CallvanReportFirstStepContent
import `in`.koreatech.koin.feature.callvan.ui.report.component.CallvanReportSecondStepContent
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportFirstStepUiState
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportSecondStepUiState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CallvanReportScreen(
    viewModel: CallvanReportViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    onShowErrorMessage: (String) -> Unit = {}
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CallvanReportSideEffect.NavigateBack -> onTopbarBackClick()
            is CallvanReportSideEffect.ShowErrorMessage -> onShowErrorMessage(sideEffect.message)
        }
    }

    CallvanReportScreenImpl(
        firstStep = CallvanReportFirstStepUiState(
            selectedReason = state.selectedReason,
            onSelectedReasonChange = viewModel::onReasonSelect,
            otherReason = state.otherReason,
            onOtherReasonChange = viewModel::onOtherReasonChange
        ),
        secondStep = CallvanReportSecondStepUiState(
            detail = state.detail,
            onDetailChange = viewModel::onDetailChange,
            images = state.images,
            onAddImageClick = {},
            onRemoveImage = viewModel::onRemoveImage
        ),
        step = state.step,
        onTopbarBackClick = viewModel::onPreviousStep,
        onNextClick = viewModel::onNextStep,
        onSubmitClick = viewModel::onSubmit
    )
}

@Composable
private fun CallvanReportScreenImpl(
    firstStep: CallvanReportFirstStepUiState = CallvanReportFirstStepUiState(),
    secondStep: CallvanReportSecondStepUiState = CallvanReportSecondStepUiState(),
    step: Int = 1,
    onTopbarBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
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
                onClick = if (step == 1) onNextClick else onSubmitClick,
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
                selectedReason = firstStep.selectedReason,
                onSelectedReasonChange = firstStep.onSelectedReasonChange,
                otherReason = firstStep.otherReason,
                onOtherReasonChange = firstStep.onOtherReasonChange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .imePadding()
            )
            2 -> CallvanReportSecondStepContent(
                detail = secondStep.detail,
                onDetailChange = secondStep.onDetailChange,
                images = secondStep.images,
                onAddImageClick = secondStep.onAddImageClick,
                onRemoveImage = secondStep.onRemoveImage,
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
    CallvanReportScreenImpl(step = 1)
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportScreenSecondPreview() {
    CallvanReportScreenImpl(step = 2)
}
