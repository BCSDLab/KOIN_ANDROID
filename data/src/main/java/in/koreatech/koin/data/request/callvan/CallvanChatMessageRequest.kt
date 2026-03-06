package `in`.koreatech.koin.data.request.callvan

import com.google.gson.annotations.SerializedName

data class CallvanChatMessageRequest(
    @SerializedName("is_image")
    val isImage: Boolean,
    @SerializedName("content")
    val content: String
)
