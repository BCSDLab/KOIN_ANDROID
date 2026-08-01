package `in`.koreatech.koin.feature.store.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
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
fun ZoomableImage(
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    onScaleChanged: (Float) -> Unit = {},
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
                            offset = calculateDoubleTapOffset(tapOffset, size)
                            scale = 2f
                        }
                        onScaleChanged(scale)
                    }
                )
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    do {
                        val event = awaitPointerEvent()
                        val pointerCount = event.changes.count { it.pressed }
                        if (pointerCount >= 2) {
                            val newScale = (scale * event.calculateZoom()).coerceIn(minScale, maxScale)
                            offset = clampOffset(offset + event.calculatePan(), size, scale)
                            scale = newScale
                            onScaleChanged(scale)
                            event.changes.forEach { it.consume() }
                        } else if (scale > 1f) {
                            offset = clampOffset(offset + event.calculatePan(), size, scale)
                            event.changes.forEach { it.consume() }
                        }
                        // scale == 1f, 단일 터치 → 소비하지 않아 HorizontalPager 가 스와이프 처리
                    } while (event.changes.any { it.pressed })
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

private fun calculateDoubleTapOffset(tapOffset: Offset, size: IntSize): Offset {
    val offsetX = (-(tapOffset.x - (size.width / 2f)) * 2f)
        .coerceIn(-size.width / 2f, size.width / 2f)
    return Offset(offsetX, 0f)
}

private fun clampOffset(offset: Offset, size: IntSize, scale: Float): Offset {
    val maxX = (size.width * (scale - 1)) / 2
    val maxY = (size.height * (scale - 1)) / scale / 2
    return Offset(
        x = offset.x.coerceIn(-maxX, maxX),
        y = offset.y.coerceIn(-maxY, maxY)
    )
}