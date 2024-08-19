package `in`.koreatech.koin.domain.state.signup

sealed class SignupContinuationState {
    object PhoneNumberDuplicated: SignupContinuationState() // 전화번호 중복
    object EmailDuplicated: SignupContinuationState() // 이메일 중복
    object NicknameDuplicated: SignupContinuationState() // 닉네임 중복
    object BusinessNumberDuplicated: SignupContinuationState() // 사업자 번호 중복
    object AvailablePhoneNumber: SignupContinuationState() // 전화번호 중복 확인으로 사용 가능
    object AvailableEmail: SignupContinuationState() // 이메일 중복 확인으로 사용 가능
    object AvailableNickname: SignupContinuationState() // 이메일 중복 확인으로 사용 가능

    object RequestedEmailValidation: SignupContinuationState()
    object RequestedSmsValidation: SignupContinuationState()
    object RequestedOwnerRegister: SignupContinuationState() // 사장님 회원가입 요청

    object CheckNickName: SignupContinuationState() // 닉네임 중복버튼 눌렀는지 확인
    object CheckGender: SignupContinuationState() // 성별 라디오 버튼 눌렀는지 확인
    object CheckGraduate: SignupContinuationState() // 졸업생 라디오 버튼을 눌렀는지 확인
    object CheckDept: SignupContinuationState() // 전공 잘 적용되었는지 확인
    object InitName: SignupContinuationState() // 이름을 작성했는지 확인
    object InitPhoneNumber: SignupContinuationState() // 전화번호를 작성했는지 확인
    object InitStudentId: SignupContinuationState() // 학번을 작성했는지 확인

    object BusinessNumberIsNotValidate: SignupContinuationState()
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
