package `in`.koreatech.koin.feature.user.model

sealed class NicknameState {
    data object NicknameAvailable : NicknameState()
    data object NicknameDuplicated : NicknameState()
    data object Failed : NicknameState()
    data object None : NicknameState()
}
