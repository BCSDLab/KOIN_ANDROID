package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun SemesterButton(
    text: String,
    modifier: Modifier = Modifier,
    isExisted: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    if (isExisted) {
        val color = if (isSelected) FilledButtonColors.Danger else FilledButtonColors.Success
        FilledButton(
            modifier = modifier,
            text = text,
            onClick = onClick,
            colors = color
        )
    } else {
        if (isSelected) {
            FilledButton(
                modifier = modifier,
                text = text,
                onClick = onClick,
                colors = FilledButtonColors.Success
            )
        } else {
            OutlinedBoxButton(
                modifier = modifier,
                text = text,
                textStyle = KoinTheme.typography.medium16,
                onClick = onClick,
                colors =
                ButtonColors(
                    containerColor = KoinTheme.colors.neutral0,
                    contentColor = KoinTheme.colors.neutral800,
                    disabledContainerColor = KoinTheme.colors.neutral300,
                    disabledContentColor = KoinTheme.colors.neutral800
                ),
                border =
                BorderStroke(
                    width = 1.dp,
                    color = KoinTheme.colors.neutral300
                )
            )
        }
    }
}
