package `in`.koreatech.koin.data.request.chat

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatReadRequest(
    @SerializedName("message_id") @SerialName("message_id") val messageId: String
)
