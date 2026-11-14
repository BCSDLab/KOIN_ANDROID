package `in`.koreatech.koin.domain.model.chat

sealed interface ChatRoomEvent {
    val roomId: String

    data class MessageCreated(
        override val roomId: String,
        val message: ChatRoomMessage
    ) : ChatRoomEvent

    data class RoomChanged(override val roomId: String) : ChatRoomEvent
}
