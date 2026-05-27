package `in`.koreatech.koin.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

object HomeChipDefaults {
    val Shape: Shape = RoundedCornerShape(100.dp)
    val ContentPadding = PaddingValues(vertical = 0.dp, horizontal = 8.dp)

    @Composable
    fun colors(
        backgroundColor: Color = Color(0xFFF5EBFF),
        textColor: Color = RebrandKoinTheme.colors.primary800
    ): HomeChipColors = HomeChipColors(
        backgroundColor = backgroundColor,
        textColor = textColor
    )
}

@Immutable
data class HomeChipColors(
    val backgroundColor: Color,
    val textColor: Color
)

@Composable
fun HomeChip(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = HomeChipDefaults.Shape,
    contentPadding: PaddingValues = HomeChipDefaults.ContentPadding,
    colors: HomeChipColors = HomeChipDefaults.colors()
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.backgroundColor)
            .padding(contentPadding)
    ) {
        Text(
            text = text,
            color = colors.textColor,
            style = RebrandKoinTheme.typography.regular10.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Preview
@Composable
private fun HomeChipPreview() {
    HomeChip(
        text = "KOIN 전용 이벤트 3곳"
    )
}
