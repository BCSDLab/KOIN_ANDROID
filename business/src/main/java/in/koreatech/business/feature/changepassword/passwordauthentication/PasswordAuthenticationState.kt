package `in`.koreatech.business.feature.changepassword.passwordauthentication

data class PasswordAuthenticationState (
    val authenticationBtnIsClicked: Boolean = false,
    val email: String = "",
    val authenticationCode: String = "",
)

fun PasswordAuthenticationState.emailIsEmpty() = email.isEmpty()

fun PasswordAuthenticationState.authCodeIsEmpty() = authenticationCode.isEmpty()
