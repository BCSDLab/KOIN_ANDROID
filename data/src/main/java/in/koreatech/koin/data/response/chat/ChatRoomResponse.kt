package `in`.koreatech.koin.data.response.chat

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.chat.ChatRoom

data class ChatRoomResponse(
    @SerializedName("article_id") val articleId: Int,
    @SerializedName("chat_room_id") val chatRoomId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("article_title") val articleTitle: String,
    @SerializedName("chat_partner_profile_image") val chatPartnerProfileImage: String?,
) {
    fun toChatRoom() =
        ChatRoom(
            articleId = articleId,
            chatRoomId = chatRoomId,
            userId = userId,
            articleTitle = articleTitle.replace("\n", " "),
            chatPartnerProfileImage = chatPartnerProfileImage,
        )
}
