package `in`.koreatech.koin.domain.state.business.changepw

sealed class ChangePasswordContinuationState {
    object SmsValidationRequested : ChangePasswordContinuationState()

    object ChangePasswordScreenNavigated : ChangePasswordContinuationState()

    object AuthCodeSent : ChangePasswordContinuationState()

    object PasswordChangeFinished : ChangePasswordContinuationState()

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

    object ToastNullAuthCode : ChangePasswordExceptionState()

    object ToastIsNotPasswordForm : ChangePasswordExceptionState()

    object NotCoincidePassword : ChangePasswordExceptionState()

    object ToastNullPassword : ChangePasswordExceptionState()

    object ToastNullPasswordChecked : ChangePasswordExceptionState()

    object NotExistsAccount : ChangePasswordExceptionState()
}
