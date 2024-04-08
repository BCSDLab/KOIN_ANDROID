package `in`.koreatech.business.feature_changepassword.changepassword

import `in`.koreatech.business.feature_changepassword.passwordauthentication.PasswordAuthenticationSideEffect

sealed class ChangePasswordSideEffect {

    object ToastNullEmail: ChangePasswordSideEffect()

    object ToastIsNotPasswordForm: ChangePasswordSideEffect()

    object ToastNullPassword: ChangePasswordSideEffect()

    object ToastNullPasswordChecked: ChangePasswordSideEffect()

    object NotCoincidePassword: ChangePasswordSideEffect()

    object GotoFinishScreen: ChangePasswordSideEffect()

}