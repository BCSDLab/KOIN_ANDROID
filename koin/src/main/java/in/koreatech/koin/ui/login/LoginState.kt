package `in`.koreatech.koin.ui.login

sealed class LoginState {
    object Init : LoginState()
    object Success : LoginState()
    data class Failed(
        val message: String = "",
    ) : LoginState()
}