package `in`.koreatech.koin.data.request.chat

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendChatMessageRequest(
    @SerializedName("content") @SerialName("content") val content: String,
    @SerializedName("is_image") @SerialName("is_image") val isImage: Boolean
)
