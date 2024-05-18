package `in`.koreatech.business.feature_changepassword.changepassword
sealed class ChangePasswordSideEffect {

    object ToastNullEmail: ChangePasswordSideEffect()

    object ToastIsNotPasswordForm: ChangePasswordSideEffect()

    object ToastNullPassword: ChangePasswordSideEffect()

    object ToastNullPasswordChecked: ChangePasswordSideEffect()

    object NotCoincidePassword: ChangePasswordSideEffect()

    object GotoFinishScreen: ChangePasswordSideEffect()

}