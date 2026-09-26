package `in`.koreatech.koin.firebase

import `in`.koreatech.koin.core.notification.MessagingNotification
import `in`.koreatech.koin.core.notification.NotificationPayload
import `in`.koreatech.koin.core.notification.NotificationResolver
import `in`.koreatech.koin.core.notification.NotificationType
import java.net.URI
import javax.inject.Inject

class ChatNotificationResolver @Inject constructor() : NotificationResolver {
    override fun resolve(payload: NotificationPayload): NotificationType? {
        val uri = payload.url.toChatUriOrNull() ?: return null
        val sender = payload.title?.removeSuffix(MESSAGE_TITLE_SUFFIX)?.takeIf(String::isNotBlank) ?: return null
        val message = payload.content?.takeIf(String::isNotBlank) ?: return null
        val stableKey = uri.queryParameter(CHAT_ROOM_ID_QUERY)?.let { "chat:room:$it" }
            ?: uri.queryParameter(ARTICLE_ID_QUERY)?.let { "chat:article:$it" }
            ?: uri.queryParameter(ID_QUERY)?.let { "chat:article:$it" }
            ?: return null

        return MessagingNotification(
            stableKey = stableKey,
            sender = sender,
            message = message
        )
    }

    private fun String?.toChatUriOrNull(): URI? {
        if (this == null) return null
        return runCatching { URI(this) }
            .getOrNull()
            ?.takeIf { it.host == CHAT_HOST }
    }

    private fun URI.queryParameter(name: String): String? =
        rawQuery
            ?.split("&")
            ?.firstOrNull { it.substringBefore("=") == name }
            ?.substringAfter("=", missingDelimiterValue = "")
            ?.takeIf(String::isNotBlank)

    private companion object {
        const val CHAT_HOST = "chat"
        const val CHAT_ROOM_ID_QUERY = "chatRoomId"
        const val ARTICLE_ID_QUERY = "articleId"
        const val ID_QUERY = "id"
        const val MESSAGE_TITLE_SUFFIX = "님의 메시지"
    }
}
