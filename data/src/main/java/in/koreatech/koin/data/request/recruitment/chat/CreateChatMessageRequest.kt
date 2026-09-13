package `in`.koreatech.koin.data.request.recruitment.chat

import com.google.gson.annotations.SerializedName

data class CreateChatMessageRequest(
    @SerializedName("content")
    val content: String,
    @SerializedName("is_image")
    val isImage: Boolean
)
