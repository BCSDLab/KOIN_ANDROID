package `in`.koreatech.business.feature_changepassword.changepassword

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangePasswordState(
    val password: String = "",
    val passwordChecked: String = "",
    val phoneNumber: String = "",
    val notCoincidePW: Boolean = false,
    val fillAllPasswords: Boolean = false
): Parcelable

fun ChangePasswordState.passwordIsBlank() = password.isBlank()
fun ChangePasswordState.passwordCheckedIsBlank() = passwordChecked.isBlank()