package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCheckbox(
    text: String,
    textStyle: TextStyle,
    isChecked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier =
        modifier.noRippleClickable {
            if (enabled) onCheckChanged(!isChecked)
        },
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            TriStateCheckbox(
                state = ToggleableState(isChecked),
                onClick = null,
                enabled = enabled,
                colors =
                CheckboxDefaults.colors().copy(
                    checkedBoxColor = KoinTheme.colors.primary500,
                    checkedCheckmarkColor = KoinTheme.colors.neutral0,
                    checkedBorderColor = KoinTheme.colors.primary500,
                    uncheckedBoxColor = KoinTheme.colors.neutral0,
                    uncheckedCheckmarkColor = KoinTheme.colors.neutral0,
                    uncheckedBorderColor = KoinTheme.colors.neutral400,
                    disabledCheckedBoxColor = KoinTheme.colors.primary300,
                    disabledBorderColor = KoinTheme.colors.primary300
                )
            )
        }
        Text(
            text = text,
            style = textStyle
        )
    }
}

@Preview()
@Composable
private fun TextCheckboxCheckedPreview() {
    KoinTheme {
        TextCheckbox(
            modifier = Modifier.background(KoinTheme.colors.neutral0),
            text = "체크박스",
            textStyle = KoinTheme.typography.regular15,
            isChecked = true,
            onCheckChanged = {}
        )
    }
}

@Preview()
@Composable
private fun TextCheckboxUncheckedPreview() {
    KoinTheme {
        TextCheckbox(
            modifier = Modifier.background(KoinTheme.colors.neutral0),
            text = "체크박스",
            textStyle = KoinTheme.typography.regular15,
            isChecked = false,
            onCheckChanged = {}
        )
    }
}

@Preview()
@Composable
private fun TextCheckboxDisabledPreview() {
    KoinTheme {
        TextCheckbox(
            modifier = Modifier.background(KoinTheme.colors.neutral0),
            text = "체크박스",
            textStyle = KoinTheme.typography.regular15,
            isChecked = true,
            onCheckChanged = {},
            enabled = false
        )
    }
}
