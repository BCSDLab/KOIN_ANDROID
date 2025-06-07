package `in`.koreatech.koin.domain.model.user

sealed class VerificationCode {
    data object None : VerificationCode()
    data object Valid : VerificationCode()
    data object NotValid : VerificationCode()
    data object Expired : VerificationCode()
}
