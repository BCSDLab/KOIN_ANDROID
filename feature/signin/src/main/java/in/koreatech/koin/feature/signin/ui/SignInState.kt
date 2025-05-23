package `in`.koreatech.koin.feature.signin.ui

data class SignInState(
    val loginId: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val isError: Boolean = false
)
