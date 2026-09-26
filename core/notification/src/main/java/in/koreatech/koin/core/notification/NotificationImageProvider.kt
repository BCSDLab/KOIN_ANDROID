package `in`.koreatech.koin.core.notification

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import timber.log.Timber

internal object NotificationImageProvider {
    fun getUri(context: Context, imageUrl: String): Image? {
        val format = imageUrl.imageFormatOrNull() ?: return null

        return runCatching {
            val imageFile = download(context, imageUrl, format.extension)
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                imageFile
            )
            Image(format.mimeType, contentUri)
        }.onFailure {
            Timber.e(it, "Notification message image download failed")
        }.getOrNull()
    }

    private fun download(context: Context, imageUrl: String, extension: String): File {
        val imageDirectory = File(context.cacheDir, CACHE_DIRECTORY).apply { mkdirs() }
        val imageFile = File(imageDirectory, "${imageUrl.hashCode()}.$extension")
        if (imageFile.isFile && imageFile.length() > 0L) return imageFile

        val temporaryFile = File(imageDirectory, "${imageFile.name}.tmp")
        val connection = URL(imageUrl).openConnection() as HttpURLConnection
        try {
            connection.inputStream.use { input ->
                temporaryFile.outputStream().use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var totalBytes = 0L
                    while (true) {
                        val readBytes = input.read(buffer)
                        if (readBytes == -1) break
                        totalBytes += readBytes
                        check(totalBytes <= MAX_IMAGE_BYTES) { "Notification image is too large" }
                        output.write(buffer, 0, readBytes)
                    }
                }
            }
            check(temporaryFile.renameTo(imageFile)) { "Failed to cache notification image" }
            return imageFile
        } catch (throwable: Throwable) {
            temporaryFile.delete()
            throw throwable
        } finally {
            connection.disconnect()
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

    private data class ImageFormat(val mimeType: String, val extension: String)

    private const val CACHE_DIRECTORY = "notification_images"
    private const val MAX_IMAGE_BYTES = 10L * 1024 * 1024
    private val IMAGE_URL_SCHEMES = setOf("http", "https")
}
