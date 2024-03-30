package `in`.koreatech.koin.domain.state.business.changepw

sealed class ChangePasswordContinuationState {
    object GotoChangePasswordScreen: ChangePasswordContinuationState()
    object ToastNoEmail: ChangePasswordContinuationState()
}