package `in`.koreatech.koin.feature.findpassword.ui.changepassword

import `in`.koreatech.koin.domain.util.ext.isValidPassword

data class ChangePasswordState(
    val password: String = "",
    val passwordConfirm: String = "",
    val showPassword: Boolean = false
)

val ChangePasswordState.isPasswordValid: Boolean
    get() = password.isValidPassword()
