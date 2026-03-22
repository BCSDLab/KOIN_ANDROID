package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanPickerFooter(
    onReset: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
    ) {
        Text(
            text = stringResource(R.string.callvan_create_picker_reset),
            style = RebrandKoinTheme.typography.medium14,
            color = RebrandKoinTheme.colors.primary500,
            modifier = Modifier.noRippleClickable(onClick = onReset)
        )
        Text(
            text = stringResource(R.string.callvan_create_picker_confirm),
            style = RebrandKoinTheme.typography.medium14,
            color = RebrandKoinTheme.colors.primary500,
            modifier = Modifier.noRippleClickable(onClick = onConfirm)
        )
    }
}
