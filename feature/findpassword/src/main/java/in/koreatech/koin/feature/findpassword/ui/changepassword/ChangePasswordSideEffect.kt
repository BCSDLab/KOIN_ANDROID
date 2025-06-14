package `in`.koreatech.koin.feature.findpassword.ui.changepassword

sealed class ChangePasswordSideEffect {
    data object PasswordChanged : ChangePasswordSideEffect()
    data object PasswordChangeFailed : ChangePasswordSideEffect()
}
