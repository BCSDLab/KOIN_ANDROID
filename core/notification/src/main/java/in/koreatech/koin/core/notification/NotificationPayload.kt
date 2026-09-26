package `in`.koreatech.koin.core.notification

import java.net.URI

data class NotificationPayload(
    val title: String?,
    val content: String?,
    val imageUrl: String?,
    val url: String?
) {
    val host: String?
        get() = url?.let { runCatching { URI(it).host }.getOrNull() }

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_CONTENT = "content"
        private const val KEY_IMAGE_URL = "imageUrl"
        private const val KEY_URL = "url"

        internal fun from(data: Map<String, String>): NotificationPayload =
            NotificationPayload(
                title = data[KEY_TITLE],
                content = data[KEY_CONTENT],
                imageUrl = data[KEY_IMAGE_URL],
                url = data[KEY_URL]
            )
    }
}
