package `in`.koreatech.koin.core.util

import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.util.DebugLogger
import `in`.koreatech.koin.core.BuildConfig

object KoinCoilImageLoader {
    fun getImageLoader(context: Context, useGif: Boolean = false): ImageLoader {
        return ImageLoader.Builder(context)
            .logger(if (BuildConfig.IS_DEBUG) DebugLogger() else null)
            .components {
                if (useGif) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
            }
            .respectCacheHeaders(false)
            .build()
    }
}
