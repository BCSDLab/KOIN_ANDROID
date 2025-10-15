package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun KoinStoreSignInDialog(
    modifier: Modifier = Modifier,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {}
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = KoinTheme.shapes.small
            )
            .padding(horizontal = 32.dp, vertical = 24.dp),
        onDismissRequest = { onNegative() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicText(
                text = stringResource(R.string.store_sign_in_dialog_title),
                style = RebrandKoinTheme.typography.medium18.merge(textAlign = TextAlign.Center, color = RebrandKoinTheme.colors.neutral600)
            )
            BasicText(
                text = stringResource(R.string.store_sign_in_dialog_message),
                style = RebrandKoinTheme.typography.regular14.merge(textAlign = TextAlign.Center, color = RebrandKoinTheme.colors.neutral500)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    modifier = Modifier.weight(1f),
                    shape = RebrandKoinTheme.shapes.small,
                    text = stringResource(R.string.store_sign_in_dialog_negative_button),
                    onClick = onNegative,
                    contentPadding = PaddingValues(vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RebrandKoinTheme.colors.neutral0,
                        contentColor = RebrandKoinTheme.colors.neutral600,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                        disabledContentColor = RebrandKoinTheme.colors.neutral600
                    ),
                    border = BorderStroke(width = 1.dp, color = KoinTheme.colors.neutral400)
                )
                FilledButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.store_sign_in_dialog_positive_button),
                    shape = RebrandKoinTheme.shapes.small,
                    onClick = onPositive,
                    contentPadding = PaddingValues(vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RebrandKoinTheme.colors.primary500,
                        contentColor = RebrandKoinTheme.colors.neutral0,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                        disabledContentColor = RebrandKoinTheme.colors.neutral600
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun KoinStoreSignInDialogPreview() {
    KoinStoreSignInDialog()
}
