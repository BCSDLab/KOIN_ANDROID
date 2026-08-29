package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DepartmentRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = if (isSelected) KoinTheme.colors.primary500 else KoinTheme.colors.neutral500
    Row(
        modifier = modifier.noRippleClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            modifier = Modifier.size(24.dp),
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = KoinTheme.colors.primary500,
                unselectedColor = KoinTheme.colors.neutral500
            )
        )
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            text = text,
            style = KoinTheme.typography.regular15.copy(
                color = textColor
            )
        )
    }
}

@Preview
@Composable
private fun DepartmentRadioButtonPreview() {
    KoinTheme {
        DepartmentRadioButton(
            text = "버튼인데여",
            isSelected = false,
            onClick = {}
        )
    }
}
