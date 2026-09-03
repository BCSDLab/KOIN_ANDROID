package `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat

sealed class RecruitmentGroupChatSideEffect {
    data object FailedToLoadChatRoom : RecruitmentGroupChatSideEffect()
    data object FailedToLoadMessages : RecruitmentGroupChatSideEffect()
    data object FailedToSendMessage : RecruitmentGroupChatSideEffect()
    data object FailedToUploadImage : RecruitmentGroupChatSideEffect()
    data object ChatRoomReadOnly : RecruitmentGroupChatSideEffect()
    data object MessageTooFast : RecruitmentGroupChatSideEffect()
}
