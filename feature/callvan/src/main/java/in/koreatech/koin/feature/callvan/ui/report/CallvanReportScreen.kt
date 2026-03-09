package `in`.koreatech.koin.feature.callvan.ui.report

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.snackbar.CustomSnackBarHost
import `in`.koreatech.koin.core.designsystem.component.snackbar.showSnackBarWithDismiss
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.report.component.CALLVAN_REPORT_IMAGE_MAX_COUNT
import `in`.koreatech.koin.feature.callvan.ui.report.component.CallvanReportFirstStepContent
import `in`.koreatech.koin.feature.callvan.ui.report.component.CallvanReportSecondStepContent
import `in`.koreatech.koin.feature.callvan.ui.report.component.rememberImagePickerLauncher
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportFirstStepUiAction
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportFirstStepUiState
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportSecondStepUiAction
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportSecondStepUiState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CallvanReportScreen(
    viewModel: CallvanReportViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {}
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CallvanReportSideEffect.SubmitSuccess -> {
                snackbarHostState.showSnackBarWithDismiss("사용자가 신고되었습니다.")
                onTopbarBackClick()
            }
            is CallvanReportSideEffect.ShowErrorMessage -> {
                snackbarHostState.showSnackBarWithDismiss(sideEffect.message)
            }
        }
    }

    val onAddImageClick = rememberImagePickerLauncher(
        currentImageCount = state.images.size,
        maxImageCount = CALLVAN_REPORT_IMAGE_MAX_COUNT,
        onImagesSelected = viewModel::onAddImages
    )

    val firstStepAction = remember {
        CallvanReportFirstStepUiAction(
            onSelectedReasonChange = viewModel::onReasonSelect,
            onOtherReasonChange = viewModel::onOtherReasonChange
        )
    }
    val secondStepAction = remember(onAddImageClick) {
        CallvanReportSecondStepUiAction(
            onDetailChange = viewModel::onDetailChange,
            onAddImageClick = onAddImageClick,
            onRemoveImage = viewModel::onRemoveImage
        )
    }

    CallvanReportScreenImpl(
        firstStepState = CallvanReportFirstStepUiState(
            selectedReason = state.selectedReason,
            otherReason = state.otherReason,
            isOtherReasonError = state.isOtherReasonError
        ),
        firstStepAction = firstStepAction,
        secondStepState = CallvanReportSecondStepUiState(
            detail = state.detail,
            images = state.images
        ),
        secondStepAction = secondStepAction,
        snackbarHostState = snackbarHostState,
        step = state.step,
        isLastStep = state.step == CallvanReportViewModel.TOTAL_STEPS,
        onTopbarBackClick = onTopbarBackClick,
        onPreviousClick = viewModel::onPreviousStep,
        onNextClick = viewModel::onNextStep,
        onSubmitClick = viewModel::onSubmit
    )
}

@Composable
private fun CallvanReportScreenImpl(
    step: Int = 1,
    isLastStep: Boolean = false,
    firstStepState: CallvanReportFirstStepUiState = CallvanReportFirstStepUiState(),
    firstStepAction: CallvanReportFirstStepUiAction = CallvanReportFirstStepUiAction(),
    secondStepState: CallvanReportSecondStepUiState = CallvanReportSecondStepUiState(),
    secondStepAction: CallvanReportSecondStepUiAction = CallvanReportSecondStepUiAction(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onTopbarBackClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.callvan_report_top_bar),
                onNavigationIconClick = if (step == 1) onTopbarBackClick else onPreviousClick
            )
        },
        bottomBar = {
            FilledButton(
                text = if (isLastStep) {
                    stringResource(R.string.callvan_detail_participant_report)
                } else {
                    stringResource(R.string.callvan_report_next)
                },
                enabled = firstStepState.selectedReason != null,
                onClick = if (isLastStep) onSubmitClick else onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500
                ),
                shape = KoinTheme.shapes.small
            )
        },
        snackbarHost = {
            CustomSnackBarHost(
                hotState = snackbarHostState,
                background = RebrandKoinTheme.colors.primary700.copy(alpha = 0.8f)
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
                isOtherReasonError = firstStepState.isOtherReasonError,
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
    CallvanReportScreenImpl(step = 1)
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportScreenSecondPreview() {
    CallvanReportScreenImpl(
        firstStepState = CallvanReportFirstStepUiState(selectedReason = CallvanReportReason.OTHER),
        step = 2,
        isLastStep = true
    )
}
