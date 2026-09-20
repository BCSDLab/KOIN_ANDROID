package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun LoadingDialog() {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        CircularProgressIndicator(color = RebrandKoinTheme.colors.neutral0)
    }
}

@Preview
@Composable
private fun LoadingDialogPreview() {
    LoadingDialog()
}
