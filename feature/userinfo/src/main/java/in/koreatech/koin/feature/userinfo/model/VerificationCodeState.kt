package `in`.koreatech.koin.feature.userinfo.model

sealed class VerificationCodeState {
    data object None : VerificationCodeState()
    data object Valid : VerificationCodeState()
    data object NotValid : VerificationCodeState()
    data object Expired : VerificationCodeState()
}
