package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Suppress("LongParameterList")
@Composable
fun CallvanConfirmBottomSheet(
    title: String,
    description: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CallvanBottomSheet(
        title = title,
        onDismiss = onDismiss,
        showCloseButton = true
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = description,
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral800
            )
            FilledButton(
                text = confirmText,
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                shape = KoinTheme.shapes.medium,
                contentPadding = PaddingValues(vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RebrandKoinTheme.colors.primary500)
            )
            OutlinedBoxButton(
                text = cancelText,
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = KoinTheme.shapes.medium,
                contentPadding = PaddingValues(vertical = 10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = KoinTheme.colors.neutral600),
                border = BorderStroke(1.dp, KoinTheme.colors.neutral300)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview
@Composable
private fun CallvanConfirmBottomSheetPreview() {
    CallvanConfirmBottomSheet(
        title = "알림을 모두 삭제할까요?",
        description = "삭제한 알림은 되돌릴 수 없습니다.",
        confirmText = "예",
        cancelText = "아니요",
        onConfirm = {},
        onDismiss = {}
    )
}
