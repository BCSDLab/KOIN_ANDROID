package `in`.koreatech.koin.core.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize

@Composable
fun PinchToZoom(
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    content: @Composable () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val animateScale by animateFloatAsState(
        targetValue = scale
    )
    val animateOffset by animateOffsetAsState(
        targetValue = offset
    )

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        if (scale != 1f) {
                            offset = Offset.Zero
                            scale = 1f
                        } else {
                            offset = calculateOffset(tapOffset, size)
                            scale = 2f
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(minScale, maxScale)
                    offset += pan

                    val maxX = (size.width * (scale - 1)) / 2
                    val maxY = (size.height * (scale - 1)) / scale / 2

                    offset = Offset(
                        x = offset.x.coerceIn(-maxX, maxX),
                        y = offset.y.coerceIn(-maxY, maxY)
                    )

                    scale = newScale
                }
            }
            .graphicsLayer(
                scaleX = animateScale,
                scaleY = animateScale,
                translationX = animateOffset.x,
                translationY = animateOffset.y,
                clip = false
            )
    ) {
        content()
    }
}

private fun calculateOffset(tapOffset: Offset, size: IntSize): Offset {
    val offsetX = (-(tapOffset.x - (size.width / 2f)) * 2f)
        .coerceIn(-size.width / 2f, size.width / 2f)
    return Offset(offsetX, 0f)
}
