package `in`.koreatech.koin.data.response.chat

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatListItemResponse(
    @SerialName("article_title")
    @SerializedName("article_title")
    val title: String,
    @SerialName("recent_message_content")
    @SerializedName("recent_message_content")
    val recentMessage: String,
    @SerialName("lost_item_image_url")
    @SerializedName("lost_item_image_url")
    val imageUrl: String?,
    @SerialName("unread_message_count")
    @SerializedName("unread_message_count")
    val unReadMessageCount: Int,
    @SerialName("last_message_at")
    @SerializedName("last_message_at")
    val lastMessageAt: String,
    @SerialName("article_id")
    @SerializedName("article_id")
    val articleId: Int,
    @SerialName("chat_room_id")
    @SerializedName("chat_room_id")
    val chatRoomId: Int
)

fun ChatListItemResponse.toChatListItem() =
    ChatListItem(
        title = title.replace("\n", " "),
        recentMessage = recentMessage,
        imageUrl = imageUrl,
        unReadMessageCount = unReadMessageCount,
        lastMessageAt = lastMessageAt,
        articleId = articleId,
        chatRoomId = chatRoomId
    )
