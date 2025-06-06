package `in`.koreatech.koin.feature.userinfo.model

sealed class NicknameState {
    data object NicknameAvailable : NicknameState()
    data object NicknameDuplicated : NicknameState()
    data object Failed : NicknameState()
    data object None : NicknameState()
}