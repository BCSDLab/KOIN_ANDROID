package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun CustomRadioButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val contentColor = RebrandKoinTheme.colors.primary500
    val borderColor = if (selected) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral500
    Box(
        modifier = modifier
            .size(20.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = borderColor,
                style = Stroke(width = 1.5.dp.toPx())
            )
            if (selected) {
                val radius = (size.minDimension - 2 * 4.dp.toPx()) / 2
                drawCircle(
                    color = contentColor,
                    radius = radius
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomRadioButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CustomRadioButton(
            true,
            modifier = Modifier,
            onClick = {}
        )
        CustomRadioButton(
            false,
            modifier = Modifier,
            onClick = {}
        )
    }
}
