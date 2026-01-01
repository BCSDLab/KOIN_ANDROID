package `in`.koreatech.koin.feature.chat.ui.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class ConvertedChatMessages(
    val localDate: LocalDate,
    val messages: List<ConvertedChatMessage>
) : Parcelable

fun List<ChatMessage>.mapToConvertedChatMessages(userId: Int): List<ConvertedChatMessages> {
    return if (isEmpty()) {
        listOf(ConvertedChatMessages(LocalDateTime.now().toLocalDate(), emptyList()))
    } else {
        map { it.toConvertedChatMessage(userId) }.groupBy { it.timestamp.toLocalDate() }.toList().map { ConvertedChatMessages(it.first, it.second) }
    }
}

fun List<ConvertedChatMessages>.appendMessage(message: ChatMessage, userId: Int): List<ConvertedChatMessages> {
    return if (isEmpty() || last().localDate < LocalDateTime.parse(message.timestamp).toLocalDate()) {
        plus(ConvertedChatMessages(LocalDateTime.parse(message.timestamp).toLocalDate(), listOf(message.toConvertedChatMessage(userId))))
    } else {
        dropLast(1).plus(ConvertedChatMessages(last().localDate, last().messages.plus(message.toConvertedChatMessage(userId))))
    }
}
