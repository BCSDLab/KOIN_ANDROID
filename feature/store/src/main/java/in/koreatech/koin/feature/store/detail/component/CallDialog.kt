package `in`.koreatech.koin.feature.store.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun CallDialog(
    phoneNumber: String,
    call: (phoneNumber: String) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    val rememberedCallRequest = remember(phoneNumber) { { call(phoneNumber) } }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(25))
                .background(RebrandKoinTheme.colors.neutral200)
                .padding(14.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0088FF),
                    contentColor = RebrandKoinTheme.colors.neutral0
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                onClick = rememberedCallRequest
            ) {
                Text(
                    text = stringResource(R.string.call_to_store_dialog_ok, phoneNumber),
                    style = RebrandKoinTheme.typography.medium16
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x29787880),
                    contentColor = RebrandKoinTheme.colors.neutral800
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                onClick = onDismissRequest
            ) {
                Text(
                    text = stringResource(R.string.call_to_store_dialog_cancel),
                    style = RebrandKoinTheme.typography.medium16
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallDialogPreview() {
    CallDialog(
        phoneNumber = "010-0000-0000"
    )
}
