package `in`.koreatech.koin.feature.user.ui.signin

data class SignInState(
    val loginId: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val loginError: LoginError = LoginError()
) {
    data class LoginError(
        val isError: Boolean = false,
        val message: String = ""
    )
}
