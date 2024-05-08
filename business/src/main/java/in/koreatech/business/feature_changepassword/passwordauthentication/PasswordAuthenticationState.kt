package `in`.koreatech.business.feature_changepassword.passwordauthentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

data class PasswordAuthenticationState (
    val authenticationBtnIsClicked: Boolean = false,
    val email: String = "",
    val authenticationCode: String = "",
)

fun PasswordAuthenticationState.emailIsEmpty() = email.isEmpty()

fun PasswordAuthenticationState.authCodeIsEmpty() = authenticationCode.isEmpty()
