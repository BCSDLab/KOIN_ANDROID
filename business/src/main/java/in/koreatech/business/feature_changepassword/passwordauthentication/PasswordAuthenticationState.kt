package `in`.koreatech.business.feature_changepassword.passwordauthentication

data class PasswordAuthenticationState (
    val authenticationBtnIsClicked: Boolean = false,
    val phoneNumber: String = "",
    val authenticationCode: String = "",
)

fun PasswordAuthenticationState.phoneNumberIsEmpty() = phoneNumber.isEmpty()

fun PasswordAuthenticationState.authCodeIsEmpty() = authenticationCode.isEmpty()
