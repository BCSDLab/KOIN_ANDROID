package `in`.koreatech.koin.feature.chat.ui.room

import android.net.Uri
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import java.time.LocalDate

data class ChatRoomState(
    val articleId: Int = 0,
    val chatRoomId: Int = 0,
    val userId: Int = 0,
    val userNickName: String = "",
    val articleTitle: String = "",
    val chatPartnerProfileImage: Uri? = null,
    val chatMessage: List<Pair<LocalDate, List<ConvertedChatMessage>>> = emptyList(),
    val chatInputValue: String = "",
    val showMenu: Boolean = false,
    val showBlockDialog: Boolean = false,
    val uploadingImage: List<ConvertedChatMessage> = emptyList(),
    val showImage: Pair<Boolean, Uri> = Pair(false, Uri.EMPTY),
    val isBlocked: Boolean = false
)
