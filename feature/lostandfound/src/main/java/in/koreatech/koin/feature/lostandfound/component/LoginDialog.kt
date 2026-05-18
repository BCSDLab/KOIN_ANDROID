package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun LoginDialog(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {}
) {
    ChoiceDialog(
        modifier = modifier,
        title = title,
        description = description,
        positiveButtonText = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in_positive),
        negativeButtonText = stringResource(id = R.string.lost_and_found_my_filter_can_use_logged_in_negative),
        onPositive = onPositive,
        onNegative = onNegative,
        titleStyle = KoinTheme.typography.medium18.copy(color = KoinTheme.colors.neutral600, textAlign = TextAlign.Center),
        descriptionStyle = KoinTheme.typography.regular14.copy(color = Color(0xFF8E8E8E))
    )
}
