package `in`.koreatech.koin.feature.dining.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    BasicAlertDialog(
        modifier =
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
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
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onDismiss() },
                    painter = painterResource(R.drawable.ic_close_round),
                    contentDescription = null,
                    tint = KoinTheme.colors.neutral0
                )
            }
            BoxWithConstraints {
                val boxWidth = constraints.maxWidth.toFloat()
                val boxHeight = constraints.maxHeight.toFloat()
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                val newScale = (scale * zoom).coerceIn(1f, 5f)
                                offset += pan

                                scale = newScale

                                val maxX = (boxWidth * (scale - 1)) / 2
                                val maxY = (boxHeight * (scale - 1)) / 2

                                offset = Offset(
                                    x = offset.x.coerceIn(-maxX, maxX),
                                    y = offset.y.coerceIn(-maxY, maxY)
                                )
                            }
                        }
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
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