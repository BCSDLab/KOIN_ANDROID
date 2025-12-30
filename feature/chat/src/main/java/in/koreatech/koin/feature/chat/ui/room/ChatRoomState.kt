package `in`.koreatech.koin.feature.chat.ui.room

import android.net.Uri
import android.os.Parcelable
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessages
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatRoomState(
    val isLoading: Boolean = true,
    val articleId: Int = 0,
    val chatRoomId: Int = 0,
    val userId: Int = 0,
    val userNickName: String = "",
    val articleTitle: String = "",
    val chatPartnerProfileImage: Uri? = null,
    val chatMessage: List<ConvertedChatMessages> = emptyList(),
    val chatInputValue: String = "",
    val showMenu: Boolean = false,
    val showBlockDialog: Boolean = false,
    val uploadingImage: List<ConvertedChatMessage> = emptyList(),
    val showImage: Pair<Boolean, Uri> = Pair(false, Uri.EMPTY),
    val isBlocked: Boolean = false
) : Parcelable
