package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun OptionGuideChip(
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    borderColor: Color,
    textColor: Color
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(percent = 50),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = RebrandKoinTheme.typography.bold12,
            color = textColor
        )
    }
}

@Preview
@Composable
fun OptionGuideChipPreview() {
    OptionGuideChip(
        label = "필수",
        modifier = Modifier,
        backgroundColor = RebrandKoinTheme.colors.neutral0,
        contentColor = RebrandKoinTheme.colors.primary300,
        borderColor = RebrandKoinTheme.colors.primary300,
        textColor = RebrandKoinTheme.colors.primary300
    )
}
