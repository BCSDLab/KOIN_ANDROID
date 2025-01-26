package `in`.koreatech.koin.feature.chat.ui.room

import android.net.Uri

data class ChatRoomState (
    val articleId: Int = 0,
    val chatRoomId: Int = 0,
    val userId: Int = 0,
    val articleTitle: String = "",
    val chatPartnerProfileImage: Uri? = null,
)