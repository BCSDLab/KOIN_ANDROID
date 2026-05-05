package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.model.CallvanRestrictionUiState
import java.time.LocalDate

@Composable
fun CallvanBanDialog(
    restrictionType: CallvanRestrictionUiState.RestrictionType,
    restrictedUntil: LocalDate?,
    onDismiss: () -> Unit
) {
    if (restrictionType == CallvanRestrictionUiState.RestrictionType.NONE) return

    val formattedDate = if (restrictedUntil != null) {
        stringResource(R.string.callvan_ban_date_format, restrictedUntil.monthValue, restrictedUntil.dayOfMonth)
    } else null

    val title = when (restrictionType) {
        CallvanRestrictionUiState.RestrictionType.TEMPORARY_14_DAYS ->
            stringResource(R.string.callvan_ban_title_temporary, restrictionType.days ?: 0)
        CallvanRestrictionUiState.RestrictionType.PERMANENT ->
            stringResource(R.string.callvan_ban_title_permanent)
        CallvanRestrictionUiState.RestrictionType.NONE -> return
    }

    val description = when (restrictionType) {
        CallvanRestrictionUiState.RestrictionType.TEMPORARY_14_DAYS ->
            stringResource(R.string.callvan_ban_description_temporary, formattedDate.orEmpty())
        CallvanRestrictionUiState.RestrictionType.PERMANENT ->
            stringResource(R.string.callvan_ban_description_permanent)
        CallvanRestrictionUiState.RestrictionType.NONE -> return
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RebrandKoinTheme.shapes.small,
            colors = CardDefaults.cardColors(containerColor = RebrandKoinTheme.colors.neutral0)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = RebrandKoinTheme.typography.medium18,
                    color = RebrandKoinTheme.colors.neutral800,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = description,
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral600,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                FilledButton(
                    text = stringResource(R.string.callvan_ban_close),
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RebrandKoinTheme.shapes.medium,
                    contentPadding = PaddingValues(vertical = 14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RebrandKoinTheme.colors.primary500)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanBanDialogTemporaryPreview() {
    RebrandKoinTheme {
        CallvanBanDialog(
            restrictionType = CallvanRestrictionUiState.RestrictionType.TEMPORARY_14_DAYS,
            restrictedUntil = LocalDate.of(2025, 3, 26),
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanBanDialogPermanentPreview() {
    RebrandKoinTheme {
        CallvanBanDialog(
            restrictionType = CallvanRestrictionUiState.RestrictionType.PERMANENT,
            restrictedUntil = null,
            onDismiss = {}
        )
    }
}