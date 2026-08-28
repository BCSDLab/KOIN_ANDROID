package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloseRecruitmentDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    RecruitmentDialog(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.recruitment_close_dialog_title),
                style = RebrandKoinTheme.typography.medium15,
                color = RebrandKoinTheme.colors.neutral700
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.recruitment_close_dialog_message),
                style = RebrandKoinTheme.typography.regular13,
                color = RebrandKoinTheme.colors.neutral500
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedBoxButton(
                    text = stringResource(R.string.recruitment_close_dialog_cancel),
                    onClick = onDismiss,
                    textStyle = RebrandKoinTheme.typography.medium16,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonColors(
                        containerColor = RebrandKoinTheme.colors.neutral0,
                        contentColor = RebrandKoinTheme.colors.neutral600,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                        disabledContentColor = RebrandKoinTheme.colors.neutral600
                    ),
                    border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral500),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledButton(
                    text = stringResource(R.string.recruitment_close_dialog_confirm),
                    onClick = onConfirm,
                    textStyle = RebrandKoinTheme.typography.medium16,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonColors(
                        containerColor = RebrandKoinTheme.colors.primary500,
                        contentColor = RebrandKoinTheme.colors.neutral0,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                        disabledContentColor = RebrandKoinTheme.colors.neutral600
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun CloseRecruitmentDialogPreview() {
    RebrandKoinTheme {
        CloseRecruitmentDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
