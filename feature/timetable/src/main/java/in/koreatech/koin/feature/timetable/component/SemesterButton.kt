package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButtonColors


@Stable
data class SemesterButtonState(
    val isAdded: Boolean = false,
    val isDeleted: Boolean = false
)

@Composable
fun SemesterButton(
    text: String,
    state: SemesterButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isAdded) {
        FilledButton(
            modifier = modifier,
            text = text,
            onClick = onClick,
            colors = FilledButtonColors.Success
        )
    } else if (state.isDeleted) {
        FilledButton(
            modifier = modifier,
            text = text,
            onClick = onClick,
            colors = FilledButtonColors.Danger
        )
    } else {
        OutlinedBoxButton(
            modifier = modifier,
            text = text,
            onClick = onClick,
            colors = OutlinedBoxButtonColors.Neutral
        )
    }
}