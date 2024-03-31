package `in`.koreatech.koin.ui.userinfo

sealed class UserInfoState {
    object Logout: UserInfoState()
    object Remove: UserInfoState()
    data class Failed(
        val message: String = ""
    ): UserInfoState()
}
