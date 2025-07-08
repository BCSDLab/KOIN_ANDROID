package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun DeleteCartDialog(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dialogMessage: String? = null,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RebrandKoinTheme.shapes.large,
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = RebrandKoinTheme.colors.neutral0,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dialogMessage.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RebrandKoinTheme.shapes.small,
                        modifier = Modifier
                            .heightIn(48.dp)
                            .widthIn(110.dp),
                        border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral500),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = RebrandKoinTheme.colors.neutral500,
                            disabledContentColor = RebrandKoinTheme.colors.neutral600
                        )
                    ) {
                        Text(text = stringResource(R.string.no))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onConfirm,
                        shape = RebrandKoinTheme.shapes.small,
                        modifier = Modifier
                            .heightIn(48.dp)
                            .widthIn(110.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RebrandKoinTheme.colors.primary500,
                            contentColor = RebrandKoinTheme.colors.neutral0,
                            disabledContainerColor = RebrandKoinTheme.colors.primary400,
                            disabledContentColor = RebrandKoinTheme.colors.neutral0
                        )
                    ) {
                        Text(text = stringResource(R.string.yes))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun DeleteCartDialogPreview() {
    DeleteCartDialog(
        onConfirm = {},
        onDismiss = {},
        dialogMessage = "테스트"
    )
}