package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DepartmentRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO::버튼 색상 수정 필요
    val containerColor = if (isSelected) KoinTheme.colors.primary100 else KoinTheme.colors.neutral0
    val borderColor = if (isSelected) KoinTheme.colors.primary500 else KoinTheme.colors.neutral500
    val textColor = if (isSelected) KoinTheme.colors.primary500 else KoinTheme.colors.neutral500
    Surface(
        modifier =
            modifier
                .selectable(
                    selected = isSelected,
                    role = null,
                    onClick = onClick,
                ),
        shape = KoinTheme.shapes.extraSmall,
        color = containerColor,
        border = BorderStroke(0.5.dp, borderColor),
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = text,
            style =
                KoinTheme.typography.regular14.copy(
                    color = textColor,
                ),
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
            onClick = {},
        )
    }
}
