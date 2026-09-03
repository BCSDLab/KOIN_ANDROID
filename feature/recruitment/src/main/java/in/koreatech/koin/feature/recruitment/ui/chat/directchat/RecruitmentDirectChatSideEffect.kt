package `in`.koreatech.koin.feature.recruitment.ui.chat.directchat

sealed class RecruitmentDirectChatSideEffect {
    data object FailedToCreateChatRoom : RecruitmentDirectChatSideEffect()
    data object DirectChatUnavailable : RecruitmentDirectChatSideEffect()
    data object FailedToLoadMessages : RecruitmentDirectChatSideEffect()
    data object FailedToSendMessage : RecruitmentDirectChatSideEffect()
    data object FailedToUploadImage : RecruitmentDirectChatSideEffect()
    data object ChatRoomReadOnly : RecruitmentDirectChatSideEffect()
    data object MessageTooFast : RecruitmentDirectChatSideEffect()
}
