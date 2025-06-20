package `in`.koreatech.koin.feature.user.model

sealed class VerificationCode {
    data object None : VerificationCode()
    data object Valid : VerificationCode()
    data object NotValid : VerificationCode()
    data object Expired : VerificationCode()
}
