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
