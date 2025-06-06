package `in`.koreatech.koin.feature.userinfo.ui.userinfoedit

sealed class UserInfoEditSideEffect {
    data object UpdateUserInfoSuccess : UserInfoEditSideEffect()
    data object UpdateUserInfoFailed : UserInfoEditSideEffect()
    data object StartTimer : UserInfoEditSideEffect()
    data object StopTimer : UserInfoEditSideEffect()
}
