package `in`.koreatech.koin.feature.dining.component.dialog

import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.dining.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiningImageDialog(
    imageModel: ImageRequest,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    var doubleTapScale by remember { mutableFloatStateOf(1f) }
    var doubleTapOffset by remember { mutableStateOf(Offset.Zero) }

    var imageWidth by remember { mutableFloatStateOf(0f) }
    var imageHeight by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(doubleTapScale) {
        animate(
            initialValue = scale,
            targetValue = doubleTapScale,
            animationSpec = tween(durationMillis = 300)
        ) { value, _ ->
            scale = value
        }
    }
    LaunchedEffect(doubleTapOffset) {
        animate(
            initialValue = offset,
            targetValue = doubleTapOffset,
            animationSpec = tween(durationMillis = 300),
            typeConverter = Offset.VectorConverter
        ) { value, _ ->
            offset = value
        }
    }

    val minScale = 1f
    val maxScale = 5f
    BasicAlertDialog(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        var newScale = scale * 2f
                        var newOffset = Offset.Zero

                        if (newScale > maxScale) {
                            newScale = 1f
                        } else {
                            val dx = (tapOffset.x - imageWidth / 2)
                            val dy = (tapOffset.y - imageHeight / 2)

                            newOffset = coerceInMaxOffset(
                                imageWidth = imageWidth,
                                imageHeight = imageHeight,
                                scale = newScale,
                                offset = Offset(
                                    x = (offset.x - dx * (newScale / scale - 1)),
                                    y = (offset.y - dy * (newScale / scale - 1))
                                )
                            )
                        }

                        doubleTapScale = newScale
                        doubleTapOffset = newOffset
                    }
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(minScale, maxScale)
                    offset += pan

                    offset = coerceInMaxOffset(
                        imageWidth = imageWidth,
                        imageHeight = imageHeight,
                        scale = newScale,
                        offset = offset
                    )

                    scale = newScale
                }
            }
            .wrapContentSize(Alignment.Center),
        onDismissRequest = { onDismiss() }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    modifier = Modifier.padding(1.dp).size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onDismiss() },
                    painter = painterResource(R.drawable.ic_close_round),
                    contentDescription = null,
                    tint = KoinTheme.colors.neutral0
                )
            }
            BoxWithConstraints {
                imageWidth = constraints.maxWidth.toFloat()
                imageHeight = constraints.maxHeight.toFloat()
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y,
                            clip = false
                        ),
                    model = imageModel,
                    contentDescription = "Dining Image",
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.Center,
                    loading = {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                )
            }
        }
    }
}

private fun coerceInMaxOffset(
    imageWidth: Float,
    imageHeight: Float,
    scale: Float,
    offset: Offset
): Offset {
    val maxX = (imageWidth * (scale - 1)) / 2
    val maxY = (imageHeight * (scale - 1)) / scale / 2

    return Offset(
        x = offset.x.coerceIn(-maxX, maxX),
        y = offset.y.coerceIn(-maxY, maxY)
    )
}
