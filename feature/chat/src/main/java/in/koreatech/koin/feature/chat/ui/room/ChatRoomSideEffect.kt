package `in`.koreatech.koin.feature.chat.ui.room

sealed class ChatRoomSideEffect {
    data object FailedToConnectWS: ChatRoomSideEffect()
    data object FailedToUploadImage: ChatRoomSideEffect()
    data object BlockUserSuccess: ChatRoomSideEffect()
    data object BlockUserFailed: ChatRoomSideEffect()
    data object BlockedByUser: ChatRoomSideEffect()
}