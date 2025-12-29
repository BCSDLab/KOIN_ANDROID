package `in`.koreatech.koin.feature.chat.ui.room.mapper

import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import `in`.koreatech.koin.feature.chat.ui.model.toConvertedChatMessage
import java.time.LocalDate
import java.time.LocalDateTime

fun List<ChatMessage>.mapToConvertedChatMessage(userId: Int): List<Pair<LocalDate, List<ConvertedChatMessage>>> {
    return if (isEmpty()) {
        listOf(Pair(LocalDateTime.now().toLocalDate(), emptyList()))
    } else {
        map { it.toConvertedChatMessage(userId) }.groupBy { it.timestamp.toLocalDate() }.toList()
    }
}

fun List<Pair<LocalDate, List<ConvertedChatMessage>>>.appendMessage(message: ChatMessage, userId: Int): List<Pair<LocalDate, List<ConvertedChatMessage>>> {
    return if (isEmpty() || last().first < LocalDateTime.parse(message.timestamp).toLocalDate()) {
        plus(Pair(LocalDateTime.parse(message.timestamp).toLocalDate(), listOf(message.toConvertedChatMessage(userId))))
    } else {
        dropLast(1).plus(Pair(last().first, last().second.plus(message.toConvertedChatMessage(userId))))
    }
}
