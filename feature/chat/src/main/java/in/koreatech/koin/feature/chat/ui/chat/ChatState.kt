package `in`.koreatech.koin.feature.chat.ui.chat

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.chat.ui.model.ChatMessage
import `in`.koreatech.koin.feature.chat.ui.model.ChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ChatState(
    val roomId: String? = null,
    val legacyRoomId: Int? = null,
    val title: String = "",
    val profileImage: String? = null,
    val canSend: Boolean = true,
    val canBlockUser: Boolean = false,
    val supportsReadReceipts: Boolean = false,
    val isLoading: Boolean = true,
    val messages: ImmutableList<ChatMessageGroup> = persistentListOf(),
    val uploadingImages: ImmutableList<ChatMessage> = persistentListOf(),
    val chatInputValue: String = "",
    val showMenu: Boolean = false,
    val showBlockDialog: Boolean = false
) {
    val isUploadingImage: Boolean get() = uploadingImages.isNotEmpty()
}
