package `in`.koreatech.koin.feature.userinfo.ui.userinfoedit

sealed class UserInfoEditSideEffect {
    data object UpdateUserInfoSuccess : UserInfoEditSideEffect()
    data object InvalidDataError : UserInfoEditSideEffect()
    data object PhoneNumberValidateRequiredError : UserInfoEditSideEffect()
    data object UnknownUserError : UserInfoEditSideEffect()
    data object NicknameOrEmailConflictError : UserInfoEditSideEffect()
    data object UnknownError : UserInfoEditSideEffect()
    data object WithdrawalSuccess : UserInfoEditSideEffect()
    data object WithdrawalError : UserInfoEditSideEffect()
    data object StartTimer : UserInfoEditSideEffect()
    data object StopTimer : UserInfoEditSideEffect()
}
