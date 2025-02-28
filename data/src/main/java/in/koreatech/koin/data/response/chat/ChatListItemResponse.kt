package `in`.koreatech.koin.data.response.chat

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.chat.ChatListItem

data class ChatListItemResponse(
    @SerializedName("article_title") val title: String,
    @SerializedName("recent_message_content") val recentMessage: String,
    @SerializedName("lost_item_image_url") val imageUrl: String?,
    @SerializedName("unread_message_count") val unReadMessageCount: Int,
    @SerializedName("last_message_at") val lastMessageAt: String,
    @SerializedName("article_id") val articleId: Int,
    @SerializedName("chat_room_id") val chatRoomId: Int
)

fun ChatListItemResponse.toChatListItem() = ChatListItem(
    title = title.replace("\n", " "),
    recentMessage = recentMessage,
    imageUrl = imageUrl,
    unReadMessageCount = unReadMessageCount,
    lastMessageAt = lastMessageAt,
    articleId = articleId,
    chatRoomId = chatRoomId
)