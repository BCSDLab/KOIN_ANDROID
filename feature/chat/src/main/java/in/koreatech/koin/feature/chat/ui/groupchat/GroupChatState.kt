package `in`.koreatech.koin.feature.chat.ui.groupchat

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.chat.ChatDomainType
import `in`.koreatech.koin.feature.chat.ui.model.ChatMessage
import `in`.koreatech.koin.feature.chat.ui.model.ChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class GroupChatState(
    val isLoading: Boolean = true,
    val domainType: ChatDomainType = ChatDomainType.CALLVAN,
    val title: String = "",
    val departure: String = "",
    val arrival: String = "",
    val departureTime: String = "",
    val currentMemberCount: Int = 0,
    val maxMemberCount: Int = 0,
    val canSend: Boolean = true,
    val readReceiptRoomId: String? = null,
    val messages: ImmutableList<ChatMessageGroup> = persistentListOf(),
    val chatInputValue: String = "",
    val uploadingImages: ImmutableList<ChatMessage> = persistentListOf()
)
