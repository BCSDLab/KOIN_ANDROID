package `in`.koreatech.koin.data.response.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomEventResponse(
    @SerialName("type") val type: Type,
    @SerialName("room_id") val roomId: String,
    @SerialName("message") val message: ChatRoomMessageResponse? = null
) {
    @Serializable
    enum class Type {
        @SerialName("MESSAGE_CREATED")
        MESSAGE_CREATED,

        @SerialName("ROOM_CHANGED")
        ROOM_CHANGED
    }
}
