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
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun DepartmentRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                selectedColor = RebrandKoinTheme.colors.primary500,
                unselectedColor = RebrandKoinTheme.colors.neutral400
            )
        )
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            text = text,
            style = RebrandKoinTheme.typography.medium15.copy(
                color = RebrandKoinTheme.colors.neutral600
            )
        )
    }
}

@Preview
@Composable
private fun DepartmentRadioButtonPreview() {
    RebrandKoinTheme {
        DepartmentRadioButton(
            text = "버튼인데여",
            isSelected = false,
            onClick = {}
        )
    }
}
