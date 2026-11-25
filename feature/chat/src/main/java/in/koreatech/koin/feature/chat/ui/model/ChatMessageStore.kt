package `in`.koreatech.koin.feature.chat.ui.model

import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import java.util.TreeMap

internal class ChatMessageStore {
    private val messages = TreeMap<String, ChatRoomMessage>(messageIdComparator)
    private var snapshot: List<ChatRoomMessage> = emptyList()

    var afterMessageId: String? = null
        private set

    fun mergeHistory(history: List<ChatRoomMessage>): List<ChatRoomMessage> {
        var changed = false
        for (message in history) {
            if (messages.put(message.messageId, message) != message) changed = true
        }
        afterMessageId = history.lastOrNull()?.messageId ?: afterMessageId
        if (changed) snapshot = messages.values.toList()
        return snapshot
    }

    fun mergeMessage(message: ChatRoomMessage): List<ChatRoomMessage> {
        if (messages.put(message.messageId, message) != message) {
            snapshot = messages.values.toList()
        }
        return snapshot
    }

    private companion object {
        val messageIdComparator: Comparator<String> = compareBy<String> { it.length }.thenBy { it }
    }
}
