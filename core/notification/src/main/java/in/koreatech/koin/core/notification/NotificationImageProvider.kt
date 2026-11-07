package `in`.koreatech.koin.core.notification

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import timber.log.Timber

internal object NotificationImageProvider {
    fun getBitmap(context: Context, imageUrl: String): Bitmap? =
        getImageFile(context, imageUrl)?.let { BitmapFactory.decodeFile(it.file.absolutePath) }

    fun getUri(context: Context, imageUrl: String): Image? {
        val imageFile = getImageFile(context, imageUrl) ?: return null

        return runCatching {
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                imageFile.file
            )
            Image(imageFile.mimeType, contentUri)
        }.onFailure {
            Timber.e(it, "Failed to create notification image URI")
        }.getOrNull()
    }

    private fun getImageFile(context: Context, imageUrl: String): ImageFile? {
        val format = imageUrl.imageFormatOrNull() ?: return null
        return runCatching {
            ImageFile(
                file = download(context, imageUrl, format.extension),
                mimeType = format.mimeType
            )
        }.onFailure {
            Timber.e(it, "Notification image download failed")
        }.getOrNull()
    }

    /**
     * Downloads [imageUrl] into the notification image cache and returns the committed file.
     *
     * The connection and working file are cleaned up in `finally`, including read failures,
     * size-limit failures, and failures when committing the cache file.
     */
    private fun download(context: Context, imageUrl: String, extension: String): File {
        val imageDirectory = File(context.cacheDir, CACHE_DIRECTORY)
        check(imageDirectory.isDirectory || imageDirectory.mkdirs()) {
            "Failed to create notification image cache"
        }
        val imageFile = File(imageDirectory, "${imageUrl.hashCode()}.$extension")
        if (imageFile.isFile && imageFile.length() > 0L) return imageFile

        val temporaryFile = File(imageDirectory, "${imageFile.name}.tmp")
        val connection = URL(imageUrl).openConnection() as HttpURLConnection
        connection.connectTimeout = 10 * 1000
        connection.readTimeout = 10 * 1000
        try {
            connection.inputStream.use { input ->
                temporaryFile.outputStream().use { output -> copyImage(input, output) }
            }
            check(temporaryFile.renameTo(imageFile)) { "Failed to cache notification image" }
            return imageFile
        } finally {
            temporaryFile.delete()
            connection.disconnect()
        }
    }

    private fun copyImage(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var totalBytes = 0L
        while (true) {
            val readBytes = input.read(buffer)
            if (readBytes == -1) return
            totalBytes += readBytes
            check(totalBytes <= MAX_IMAGE_BYTES) { "Notification image is too large" }
            output.write(buffer, 0, readBytes)
        }
    }

    private fun String.imageFormatOrNull(): ImageFormat? {
        val uri = runCatching { URI(this) }.getOrNull() ?: return null
        if (uri.scheme?.lowercase() !in IMAGE_URL_SCHEMES) return null

        return when (uri.path.orEmpty().substringAfterLast('.', missingDelimiterValue = "").lowercase()) {
            "jpg", "jpeg" -> ImageFormat("image/jpeg", "jpg")
            "png" -> ImageFormat("image/png", "png")
            "gif" -> ImageFormat("image/gif", "gif")
            "webp" -> ImageFormat("image/webp", "webp")
            "bmp" -> ImageFormat("image/bmp", "bmp")
            "heic" -> ImageFormat("image/heic", "heic")
            "heif" -> ImageFormat("image/heif", "heif")
            else -> null
        }
    }

    data class Image(val mimeType: String, val uri: Uri)

    private data class ImageFile(val file: File, val mimeType: String)

    private data class ImageFormat(val mimeType: String, val extension: String)

    private const val CACHE_DIRECTORY = "notification_images"
    private const val MAX_IMAGE_BYTES = 10L * 1024 * 1024
    private val IMAGE_URL_SCHEMES = setOf("http", "https")
}
