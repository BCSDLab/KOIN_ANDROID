package `in`.koreatech.koin.feature.article.util

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun Modifier.horizontalFadingEdge(
    scrollState: ScrollState,
    length: Dp,
    edgeColor: Color = Color.White
) = composed {
    val color = edgeColor

    drawWithContent {
        val lengthValue = length.toPx()
        val scrollFromStart = scrollState.value
        val scrollFromEnd = scrollState.maxValue - scrollState.value

        val startFadingEdgeStrength = lengthValue * (scrollFromStart / lengthValue).coerceAtMost(1f)

        val endFadingEdgeStrength = lengthValue * (scrollFromEnd / lengthValue).coerceAtMost(1f)

        drawContent()

        drawRect(
            brush =
            Brush.horizontalGradient(
                colors =
                listOf(
                    color,
                    Color.Transparent
                ),
                startX = 0f,
                endX = startFadingEdgeStrength
            ),
            size =
            Size(
                startFadingEdgeStrength,
                this.size.height
            )
        )

        drawRect(
            brush =
            Brush.horizontalGradient(
                colors =
                listOf(
                    Color.Transparent,
                    color
                ),
                startX = size.width - endFadingEdgeStrength,
                endX = size.width
            ),
            topLeft = Offset(x = size.width - endFadingEdgeStrength, y = 0f)
        )
    }
}
