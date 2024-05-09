package `in`.koreatech.koin.domain.state.business.changepw

sealed class ChangePasswordContinuationState {
    object GotoChangePasswordScreen: ChangePasswordContinuationState()
    object SendAuthCode: ChangePasswordContinuationState()

    object FinishedChangePassword: ChangePasswordContinuationState()
}

sealed class ChangePasswordExceptionState: Throwable() {
    object ToastNullEmail : ChangePasswordExceptionState()
    object ToastIsNotEmail : ChangePasswordExceptionState()

    object ToastNullAuthCode: ChangePasswordExceptionState()
    object ToastIsNotPasswordForm: ChangePasswordExceptionState()

    object NotCoincidePassword: ChangePasswordExceptionState()
    object ToastNullPassword: ChangePasswordExceptionState()
    object ToastNullPasswordChecked: ChangePasswordExceptionState()

}