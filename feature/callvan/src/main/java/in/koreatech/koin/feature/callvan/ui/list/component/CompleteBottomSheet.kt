package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanConfirmBottomSheet

@Composable
fun CompleteBottomSheet(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CallvanConfirmBottomSheet(
        title = stringResource(R.string.callvan_complete_title),
        description = stringResource(R.string.callvan_complete_description),
        confirmText = stringResource(R.string.callvan_confirm_positive),
        cancelText = stringResource(R.string.callvan_confirm_negative),
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Preview
@Composable
private fun CompleteBottomSheetPreview() {
    RebrandKoinTheme {
        CompleteBottomSheet(onConfirm = {}, onDismiss = {})
    }
}
