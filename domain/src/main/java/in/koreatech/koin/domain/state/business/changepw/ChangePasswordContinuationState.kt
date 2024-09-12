package `in`.koreatech.koin.domain.state.business.changepw

sealed class ChangePasswordContinuationState {
    object RequestedSmsValidation : ChangePasswordContinuationState()
    object GotoChangePasswordScreen: ChangePasswordContinuationState()
    object SendAuthCode: ChangePasswordContinuationState()
    object FinishedChangePassword: ChangePasswordContinuationState()

    data class Failed(
        val message: String = "",
        val throwable: Throwable? = null
    ) : ChangePasswordContinuationState()
}

sealed class ChangePasswordExceptionState : Throwable() {
    object ToastNullEmail : ChangePasswordExceptionState()
    object ToastIsNotEmail : ChangePasswordExceptionState()

    object ToastNullPhoneNumber : ChangePasswordExceptionState()
    object ToastIsNotPhoneNumber : ChangePasswordExceptionState()


    object ToastNullAuthCode: ChangePasswordExceptionState()
    object ToastIsNotPasswordForm: ChangePasswordExceptionState()

    object NotCoincidePassword: ChangePasswordExceptionState()
    object ToastNullPassword: ChangePasswordExceptionState()
    object ToastNullPasswordChecked: ChangePasswordExceptionState()
    object NotExistsAccount : ChangePasswordExceptionState()
}