package `in`.koreatech.koin.feature.findpassword.ui.changepassword

import android.os.Parcelable
import `in`.koreatech.koin.domain.util.ext.isValidPassword
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangePasswordState(
    val verificationMethod: String = "",
    val email: String = "",
    val loginId: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val showPassword: Boolean = false,
    val findBySms: Boolean = true
) : Parcelable

val ChangePasswordState.isPasswordValid: Boolean
    get() = password.isValidPassword()
