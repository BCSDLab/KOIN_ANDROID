package `in`.koreatech.koin.feature.chat.ui.chat

sealed interface ChatSideEffect {
    data object FailedToConnect : ChatSideEffect
    data object FailedToCreateRoom : ChatSideEffect
    data object FailedToLoadMessages : ChatSideEffect
    data object FailedToSendMessage : ChatSideEffect
    data object FailedToUploadImage : ChatSideEffect
    data object DirectChatUnavailable : ChatSideEffect
    data object BlockUserSuccess : ChatSideEffect
    data object BlockUserFailed : ChatSideEffect
    data object BlockedByUser : ChatSideEffect
}
