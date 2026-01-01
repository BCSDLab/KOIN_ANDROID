package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    modifier: Modifier = Modifier,
    buttonText: String = stringResource(R.string.ok),
    onDismissRequest: () -> Unit = {},
    onClick: () -> Unit = { onDismissRequest() }
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = modifier
                .clip(RebrandKoinTheme.shapes.small)
                .background(RebrandKoinTheme.colors.neutral0, RebrandKoinTheme.shapes.small)
                .padding(vertical = 24.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    .background(RebrandKoinTheme.colors.primary500, RebrandKoinTheme.shapes.extraSmall)
                    .clickable {
                        onClick()
                    }
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
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

@Composable
fun KoinStoreDialog(
    message: String,
    modifier: Modifier = Modifier,
    positiveText: String = stringResource(R.string.ok),
    negativeText: String = stringResource(R.string.cancel),
    onDismissRequest: () -> Unit = {},
    onPositiveClick: () -> Unit = onDismissRequest,
    onNegativeClick: () -> Unit = onDismissRequest
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = modifier
                .clip(RebrandKoinTheme.shapes.small)
                .background(RebrandKoinTheme.colors.neutral0, RebrandKoinTheme.shapes.small)
                .padding(vertical = 24.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicText(
                text = message,
                style = RebrandKoinTheme.typography.regular15.copy(
                    color = RebrandKoinTheme.colors.neutral600,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RebrandKoinTheme.shapes.extraSmall)
                        .border(width = 1.dp, color = RebrandKoinTheme.colors.neutral500, shape = RebrandKoinTheme.shapes.extraSmall)
                        .background(RebrandKoinTheme.colors.neutral0, RebrandKoinTheme.shapes.extraSmall)
                        .clickable(onClick = onNegativeClick)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = negativeText,
                        style = RebrandKoinTheme.typography.regular15.copy(
                            color = RebrandKoinTheme.colors.neutral600,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RebrandKoinTheme.shapes.extraSmall)
                        .background(RebrandKoinTheme.colors.primary500, RebrandKoinTheme.shapes.extraSmall)
                        .clickable(onClick = onPositiveClick)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = positiveText,
                        style = RebrandKoinTheme.typography.regular15.copy(
                            color = RebrandKoinTheme.colors.neutral0,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KoinStoreDialogPreview() {
    KoinStoreDialog(
        onDismissRequest = {},
        message = "This is a preview of the Koin Store Dialog."
    )
}
