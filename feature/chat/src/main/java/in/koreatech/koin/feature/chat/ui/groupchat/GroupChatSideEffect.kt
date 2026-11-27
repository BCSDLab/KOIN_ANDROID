package `in`.koreatech.koin.feature.chat.ui.groupchat

sealed class GroupChatSideEffect {
    data object FailedToLoadChatRoom : GroupChatSideEffect()
    data object FailedToLoadMessages : GroupChatSideEffect()
    data object FailedToSendMessage : GroupChatSideEffect()
    data object FailedToUploadImage : GroupChatSideEffect()
}
