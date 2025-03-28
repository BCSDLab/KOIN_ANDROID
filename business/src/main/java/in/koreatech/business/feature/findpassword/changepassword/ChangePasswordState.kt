package `in`.koreatech.business.feature.findpassword.changepassword

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangePasswordState(
    val password: String = "",
    val passwordChecked: String = "",
    val phoneNumber: String = "",
    val notCoincidePW: Boolean = false,
    val fillAllPasswords: Boolean = false
) : Parcelable {
    val isButtonEnabled: Boolean
        get() = password.isNotEmpty() && passwordChecked.isNotEmpty() && !notCoincidePW
}

fun ChangePasswordState.passwordIsBlank() = password.isBlank()

fun ChangePasswordState.passwordCheckedIsBlank() = passwordChecked.isBlank()
