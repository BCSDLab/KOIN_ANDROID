package `in`.koreatech.business.util

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    painterResource: Int,
    imageSize: Size = Size.ORIGINAL,
) {
    val context = LocalContext.current
    val imageLoader =
        ImageLoader.Builder(context)
            .components {
                add(ImageDecoderDecoder.Factory())
            }
            .build()

    Image(
        modifier = modifier,
        painter =
            rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data(data = painterResource)
                    .apply(block = {
                        imageSize
                    }).build(),
                imageLoader = imageLoader,
            ),
        contentDescription = "",
    )
}
