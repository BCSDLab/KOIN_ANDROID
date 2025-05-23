package `in`.koreatech.koin.feature.signin.ui

data class SignInState(
    val loginId: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val loginError: LoginError = LoginError(),
    val isSuccess: Boolean = false
) {
    data class LoginError(
        val isError: Boolean = false,
        val message: String = ""
    )
}
