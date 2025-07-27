package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun KoinStoreDialog(
    message: String,
    buttonText: String = stringResource(R.string.ok),
    onDismissRequest: () -> Unit = {},
    onClick: () -> Unit = { onDismissRequest() }
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .clip(RebrandKoinTheme.shapes.small)
                .background(RebrandKoinTheme.colors.neutral0, RebrandKoinTheme.shapes.small)
                .padding(vertical = 24.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BasicText(
                text = message,
                style = RebrandKoinTheme.typography.regular15.copy(
                    color = RebrandKoinTheme.colors.neutral600,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RebrandKoinTheme.shapes.extraSmall)
                    .background(RebrandKoinTheme.colors.primary500, RebrandKoinTheme.shapes.extraSmall),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    modifier = Modifier.clickable {
                        onClick()
                    }.padding(12.dp),
                    text = buttonText,
                    style = RebrandKoinTheme.typography.regular15.copy(
                        color = RebrandKoinTheme.colors.neutral0,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun KoinStoreDialogPreview() {
    KoinStoreDialog(
        onDismissRequest = {},
        message = "This is a preview of the Koin Store Dialog.",
    )
}
