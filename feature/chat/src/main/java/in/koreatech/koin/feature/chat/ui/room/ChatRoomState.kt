package `in`.koreatech.koin.feature.chat.ui.room

import android.net.Uri
import android.view.Menu
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
)
