package `in`.koreatech.koin.feature.signin.ui

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
