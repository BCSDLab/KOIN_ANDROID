package `in`.koreatech.koin.feature.user.ui.findpassword.changepassword

sealed class ChangePasswordSideEffect {
    data object PasswordChanged : ChangePasswordSideEffect()
    data object PasswordChangeFailed : ChangePasswordSideEffect()
}
