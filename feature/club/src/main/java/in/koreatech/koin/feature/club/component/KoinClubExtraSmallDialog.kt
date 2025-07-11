package `in`.koreatech.koin.feature.club.component

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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButtonColors
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinClubExtraSmallDialog(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    titleColor: Color = KoinClubExtraSmallDialogDefaults.textColor,
    descriptionColor: Color = KoinClubExtraSmallDialogDefaults.textColor,
    titleStyle: TextStyle = KoinClubExtraSmallDialogDefaults.titleStyle,
    descriptionStyle: TextStyle = KoinClubExtraSmallDialogDefaults.descriptionStyle,
    positiveButtonText: String = stringResource(id = R.string.club_dialog_ok),
    negativeButtonText: String = stringResource(id = R.string.club_dialog_cancel),
    positiveButtonColors: FilledButtonColors = FilledButtonColors.Primary,
    negativeButtonColors: OutlinedBoxButtonColors = OutlinedBoxButtonColors.Primary,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {},
    onDismiss: () -> Unit = {}
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
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = titleStyle,
                    color = titleColor
                )
            }
            Text(
                text = description,
                style = descriptionStyle,
                color = descriptionColor
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinClubExtraSmallDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    titleColor: Color = KoinClubExtraSmallDialogDefaults.textColor,
    descriptionColor: Color = KoinClubExtraSmallDialogDefaults.textColor,
    titleStyle: TextStyle = KoinClubExtraSmallDialogDefaults.titleStyle,
    descriptionStyle: TextStyle = KoinClubExtraSmallDialogDefaults.descriptionStyle,
    positiveButtonText: String = stringResource(id = R.string.club_dialog_ok),
    negativeButtonText: String = stringResource(id = R.string.club_dialog_cancel),
    positiveButtonColors: ButtonColors = KoinClubExtraSmallDialogDefaults.positiveButtonColors,
    negativeButtonColors: ButtonColors = KoinClubExtraSmallDialogDefaults.negativeButtonColors,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {},
    onDismiss: () -> Unit = {}
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
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = titleStyle,
                    color = titleColor
                )
            }
            Text(
                text = description,
                style = descriptionStyle,
                color = descriptionColor
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
                    border = BorderStroke(
                        1.0.dp,
                        color = KoinTheme.colors.neutral500
                    ),
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

object KoinClubExtraSmallDialogDefaults {
    val textColor @Composable get() = KoinTheme.colors.neutral800
    val titleStyle @Composable get() = KoinTheme.typography.medium18
    val descriptionStyle @Composable get() = KoinTheme.typography.regular14
    val positiveButtonColors @Composable get() = ButtonColors(
        containerColor = KoinTheme.colors.primary500,
        contentColor = KoinTheme.colors.neutral0,
        disabledContainerColor = KoinTheme.colors.neutral300,
        disabledContentColor = KoinTheme.colors.neutral600
    )
    val negativeButtonColors @Composable get() = ButtonColors(
        containerColor = KoinTheme.colors.neutral0,
        contentColor = KoinTheme.colors.neutral500,
        disabledContainerColor = KoinTheme.colors.neutral400,
        disabledContentColor = KoinTheme.colors.neutral500
    )
}

object KoinClubExtraSmallDialogDanger {
    val positiveButtonColors @Composable get() = ButtonColors(
        containerColor = KoinTheme.colors.danger500,
        contentColor = KoinTheme.colors.neutral0,
        disabledContainerColor = KoinTheme.colors.neutral300,
        disabledContentColor = KoinTheme.colors.neutral600
    )
}
