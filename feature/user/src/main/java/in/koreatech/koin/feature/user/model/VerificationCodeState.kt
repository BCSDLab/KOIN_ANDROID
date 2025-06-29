package `in`.koreatech.koin.feature.user.model

sealed class VerificationCodeState {
    data object None : VerificationCodeState()
    data object Valid : VerificationCodeState()
    data object NotValid : VerificationCodeState()
    data object Expired : VerificationCodeState()
}
