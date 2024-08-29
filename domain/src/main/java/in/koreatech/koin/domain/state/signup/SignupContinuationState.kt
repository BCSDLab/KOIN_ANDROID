package `in`.koreatech.koin.domain.state.signup

sealed class SignupContinuationState {
    object EmailDuplicated: SignupContinuationState() // 이메일 중복
    object NicknameDuplicated: SignupContinuationState() // 닉네임 중복
    object AvailableEmail: SignupContinuationState() // 이메일 중복 확인으로 사용 가능
    object AvailableNickname: SignupContinuationState() // 이메일 중복 확인으로 사용 가능

    object RequestedEmailValidation: SignupContinuationState()
    object RequestedSmsValidation: SignupContinuationState()
    object CheckNickNameDuplication: SignupContinuationState() // 닉네임 중복 검사를 했는지 확인
    object CheckNameFormat: SignupContinuationState() // 이름을 올바른 형식으로 작성했는지 확인
    object CheckPhoneNumberFormat: SignupContinuationState() // 전화번호를 올바른 형식으로 작성했는지 확인

    object SmsCodeIsNotValidate: SignupContinuationState()
    object PhoneNumberIsNotValidate: SignupContinuationState()
    object EmailIsNotValidate: SignupContinuationState()
    object PasswordIsNotValidate: SignupContinuationState()
    object PasswordNotMatching: SignupContinuationState()
    object NotAgreedPrivacyTerms: SignupContinuationState()
    object NotAgreedKoinTerms: SignupContinuationState()
    object CheckComplete: SignupContinuationState()

    data class Failed(
        val message: String = "",
        val throwable: Throwable? = null
    ): SignupContinuationState()
}
