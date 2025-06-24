package `in`.koreatech.koin.domain.state.signup

sealed class SignupContinuationState {
    // 중복 상태
    object PhoneNumberDuplicated : SignupContinuationState() // 전화번호 중복
    object EmailDuplicated : SignupContinuationState() // 이메일 중복
    object NicknameDuplicated : SignupContinuationState() // 닉네임 중복
    object BusinessNumberDuplicated : SignupContinuationState() // 사업자 번호 중복

    // 사용 가능 상태
    object PhoneNumberAvailable : SignupContinuationState() // 전화번호 중복 확인으로 사용 가능
    object EmailAvailable : SignupContinuationState() // 이메일 중복 확인으로 사용 가능
    object NicknameAvailable : SignupContinuationState() // 닉네임 중복 확인으로 사용 가능

    // 유효성 검사 요청
    object EmailValidationRequested : SignupContinuationState()
    object SmsValidationRequested : SignupContinuationState()
    object OwnerRegistrationRequested : SignupContinuationState() // 사장님 회원가입 요청

    // 형식 검사
    object NameFormatChecked : SignupContinuationState() // 이름을 올바른 형식으로 작성했는지 확인
    object PhoneNumberFormatChecked : SignupContinuationState() // 전화번호를 올바른 형식으로 작성했는지 확인
    object NicknameDuplicationCheck : SignupContinuationState() // 닉네임 중복 검사를 했는지 확인

    // 유효성 검사
    object PhoneNumberInvalid : SignupContinuationState()
    object SmsCodeInvalid : SignupContinuationState()
    object EmailInvalid : SignupContinuationState()
    object PasswordInvalid : SignupContinuationState()
    object PasswordNotMatching : SignupContinuationState()
    object CompanyNumberInvalid : SignupContinuationState()
    object CompanyNumberIsDuplicated : SignupContinuationState()

    // 코인 약관 동의 상태
    object PrivacyTermsNotAgreed : SignupContinuationState()
    object KoinTermsNotAgreed : SignupContinuationState()

    // 기타
    object FilesUploadSuccess : SignupContinuationState()
    object SignupCheckComplete : SignupContinuationState()

    data class Failed(
        val message: String = "",
        val throwable: Throwable? = null
    ) : SignupContinuationState()
}
