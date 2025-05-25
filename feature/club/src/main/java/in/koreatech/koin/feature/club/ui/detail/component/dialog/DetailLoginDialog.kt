package `in`.koreatech.koin.feature.club.ui.detail.component.dialog

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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButtonColors
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailLoginDialog(
    title: String,
    description: String,
    onPositive: () -> Unit,
    onNegative: () -> Unit,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = KoinTheme.typography.medium18,
    descriptionStyle: TextStyle = KoinTheme.typography.regular14,
    positiveButtonText: String = stringResource(id = R.string.detail_dialog_login_positive),
    negativeButtonText: String = stringResource(id = R.string.common_cancellation),
    positiveButtonColors: FilledButtonColors = FilledButtonColors.Primary,
    negativeButtonColors: OutlinedBoxButtonColors = OutlinedBoxButtonColors.Primary
) {
    BasicAlertDialog(
        modifier =
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = KoinTheme.shapes.extraSmall
            )
            .padding(horizontal = 32.dp, vertical = 24.dp),
        onDismissRequest = { onNegative() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = titleStyle
            )
            Text(
                text = description,
                style = descriptionStyle
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    modifier = Modifier.weight(1.0F),
                    text = negativeButtonText,
                    onClick = onNegative,
                    colors = negativeButtonColors,
                    contentPadding = PaddingValues(vertical = 12.dp)
                )
                FilledButton(
                    modifier = Modifier.weight(1.0F),
                    text = positiveButtonText,
                    onClick = onPositive,
                    colors = positiveButtonColors,
                    contentPadding = PaddingValues(vertical = 12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun DetailLoginDialogPreview() {
    DetailLoginDialog(
        title = "로그인이 필요한 기능입니다.",
        onPositive = {},
        onNegative = {},
        description = "로그인 하시겠어요?"
    )
}
