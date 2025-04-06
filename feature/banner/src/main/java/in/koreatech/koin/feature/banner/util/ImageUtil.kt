package `in`.koreatech.koin.feature.banner.util

import androidx.annotation.IntRange

/**
 * ImageUtil
 */
object ImageUtil {
    /**
     * Return resized image url with given width, height, format and quality.
     * Don't define width and height at the same time.
     * Image will cropped if both width and height are defined.
     * @param imageUrl Original image url
     * @param width Width of the image
     * @param height Height of the image
     * @param format Format of the image
     * @param quality Quality of the image 0-100
     * @see [ImageFormat]
     * @return Resized image url
     */
    fun getResizedImageUrl(
        imageUrl: String,
        width: Int = 0,
        height: Int = 0,
        format: ImageFormat = ImageFormat.WEBP,
        @IntRange(from = 0, to = 100) quality: Int = 100
    ): String {
        return "$imageUrl?w=$width&h=$height&format=${format.name}&q=$quality"
    }
}

enum class ImageFormat {
    PNG,
    JPEG,
    WEBP,
    GIF
}
