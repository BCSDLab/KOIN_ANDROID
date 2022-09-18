package `in`.koreatech.koin.ui.userinfo.state

data class NicknameState(
    val nickname: String,
    val isNicknameDuplicated: Boolean? = null
) {
    companion object {
        fun newNickname(nickname: String) = NicknameState(nickname)
    }
}
