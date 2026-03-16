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
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanBottomSheet

@Composable
fun LoginBottomSheet(
    onLogin: () -> Unit,
    onDismiss: () -> Unit
) {
    CallvanBottomSheet(
        title = stringResource(R.string.callvan_login_title),
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
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
                shape = KoinTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500
                )
            ) {
                Text(
                    text = stringResource(R.string.callvan_login_login),
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
                    text = stringResource(R.string.callvan_login_close),
                    style = KoinTheme.typography.medium16
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginBottomSheetPreview() {
    CallvanBottomSheet(
        title = "콜밴팟에 참여하려면 로그인이 필요해요.",
        onDismiss = {},
        showCloseButton = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = KoinTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500
                )
            ) {
                Text(text = "로그인하기", style = KoinTheme.typography.medium16)
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = KoinTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                border = BorderStroke(1.dp, KoinTheme.colors.neutral300),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = KoinTheme.colors.neutral600
                )
            ) {
                Text(text = "닫기", style = KoinTheme.typography.medium16)
            }
        }
    }
}
