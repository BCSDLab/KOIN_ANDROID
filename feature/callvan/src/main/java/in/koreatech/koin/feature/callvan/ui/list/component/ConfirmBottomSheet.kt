package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.enums.ConfirmType
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanBottomSheet

@Composable
fun ConfirmBottomSheet(
    confirmType: ConfirmType,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String = when (confirmType) {
        ConfirmType.JOIN -> stringResource(R.string.callvan_confirm_join_title)
        ConfirmType.CANCEL_JOIN -> stringResource(R.string.callvan_confirm_cancel_title)
        ConfirmType.CLOSE -> stringResource(R.string.callvan_confirm_close_title)
        ConfirmType.REOPEN -> stringResource(R.string.callvan_confirm_reopen_title)
    },
) {
    CallvanBottomSheet(
        title = title,
        onDismiss = onDismiss,
        showCloseButton = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                shape = KoinTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500
                )
            ) {
                Text(
                    text = stringResource(R.string.callvan_confirm_positive),
                    style = KoinTheme.typography.medium16
                )
            }
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = KoinTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                border = BorderStroke(1.dp, KoinTheme.colors.neutral300),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = KoinTheme.colors.neutral600
                )
            ) {
                Text(
                    text = stringResource(R.string.callvan_confirm_negative),
                    style = KoinTheme.typography.medium16
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmBottomSheetJoinPreview() {
    RebrandKoinTheme {
        ConfirmBottomSheet(
            confirmType = ConfirmType.JOIN,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmBottomSheetCancelJoinPreview() {
    RebrandKoinTheme {
        ConfirmBottomSheet(
            confirmType = ConfirmType.CANCEL_JOIN,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmBottomSheetClosePreview() {
    RebrandKoinTheme {
        ConfirmBottomSheet(
            confirmType = ConfirmType.CLOSE,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmBottomSheetReopenPreview() {
    RebrandKoinTheme {
        ConfirmBottomSheet(
            confirmType = ConfirmType.REOPEN,
            onConfirm = {},
            onDismiss = {}
        )
    }
}
