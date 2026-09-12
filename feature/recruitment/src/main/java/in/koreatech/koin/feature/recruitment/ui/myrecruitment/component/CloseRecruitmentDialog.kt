package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentConfirmDialog

@Composable
fun CloseRecruitmentDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    RecruitmentConfirmDialog(
        title = stringResource(R.string.recruitment_close_dialog_title),
        message = stringResource(R.string.recruitment_close_dialog_message),
        confirmText = stringResource(R.string.recruitment_close_dialog_confirm),
        cancelText = stringResource(R.string.recruitment_close_dialog_cancel),
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun CloseRecruitmentDialogPreview() {
    RebrandKoinTheme {
        CloseRecruitmentDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
