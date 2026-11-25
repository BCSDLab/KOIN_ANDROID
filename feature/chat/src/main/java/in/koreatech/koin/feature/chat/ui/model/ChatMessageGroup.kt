package `in`.koreatech.koin.feature.chat.ui.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ChatMessageGroup(
    val date: LocalDate,
    val messages: ImmutableList<ChatMessage>
)
