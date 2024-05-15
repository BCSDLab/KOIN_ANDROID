package `in`.koreatech.business.feature.changepassword.changepassword

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangePasswordState(
    val password: String = "",
    val passwordChecked: String = "",
    val email: String = "",
    val notCoincidePW: Boolean = false,
    val PasswordsFieldIsValidate: Boolean = false
): Parcelable

fun ChangePasswordState.passwordIsBlank() = password.isBlank()
fun ChangePasswordState.passwordCheckedIsBlank() = passwordChecked.isBlank()