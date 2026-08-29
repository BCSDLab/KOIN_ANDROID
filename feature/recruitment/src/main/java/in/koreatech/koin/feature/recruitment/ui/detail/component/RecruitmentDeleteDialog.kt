package `in`.koreatech.koin.feature.recruitment.ui.detail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = RebrandKoinTheme.colors.neutral0, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 32.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.recruitment_delete_dialog_message),
                textAlign = TextAlign.Center,
                style = RebrandKoinTheme.typography.medium15.copy(
                    color = RebrandKoinTheme.colors.neutral600
                )
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedBoxButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    text = stringResource(R.string.recruitment_delete_dialog_cancel),
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        containerColor = RebrandKoinTheme.colors.neutral0,
                        contentColor = RebrandKoinTheme.colors.neutral600,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                        disabledContentColor = RebrandKoinTheme.colors.neutral500
                    ),
                    border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral500),
                    contentPadding = PaddingValues(12.dp)
                )
                FilledButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    text = stringResource(R.string.recruitment_delete_dialog_confirm),
                    onClick = onConfirm,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        containerColor = RebrandKoinTheme.colors.primary500,
                        contentColor = RebrandKoinTheme.colors.neutral0,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                        disabledContentColor = RebrandKoinTheme.colors.neutral0
                    ),
                    contentPadding = PaddingValues(12.dp)
                )
            }
        }
    }
}
