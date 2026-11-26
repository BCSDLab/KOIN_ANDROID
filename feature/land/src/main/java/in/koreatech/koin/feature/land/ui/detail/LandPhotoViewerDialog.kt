package `in`.koreatech.koin.feature.land.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import `in`.koreatech.koin.feature.land.R
import kotlin.math.min

@Composable
internal fun LandPhotoViewerDialog(
    imageUrl: String,
    contentDescription: String,
    onDismiss: () -> Unit
) {
    var scale by remember(imageUrl) { mutableFloatStateOf(1f) }
    val closeDescription = stringResource(R.string.land_close)
    var offsetX by remember(imageUrl) { mutableFloatStateOf(0f) }
    var offsetY by remember(imageUrl) { mutableFloatStateOf(0f) }
    var imageSize by remember(imageUrl) { mutableStateOf(Size.Zero) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clipToBounds()
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                onSuccess = { imageSize = it.painter.intrinsicSize },
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(imageUrl, imageSize) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            if (imageSize.width > 0f && imageSize.height > 0f) {
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                val fitScale = min(size.width / imageSize.width, size.height / imageSize.height)
                                offsetX = clampLandPhotoOffset(offsetX + pan.x, imageSize.width * fitScale, size.width.toFloat(), scale)
                                offsetY = clampLandPhotoOffset(offsetY + pan.y, imageSize.height * fitScale, size.height.toFloat(), scale)
                            }
                        }
                    }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offsetX
                        translationY = offsetY
                    }
            )
            Text(
                text = stringResource(R.string.land_close),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .semantics { this.contentDescription = closeDescription }
                    .clickable(onClick = onDismiss)
            )
        }
    }
}

internal fun clampLandPhotoOffset(offset: Float, imageSize: Float, viewportSize: Float, scale: Float): Float {
    val maxOffset = ((imageSize * scale - viewportSize) / 2f).coerceAtLeast(0f)
    return offset.coerceIn(-maxOffset, maxOffset)
}
