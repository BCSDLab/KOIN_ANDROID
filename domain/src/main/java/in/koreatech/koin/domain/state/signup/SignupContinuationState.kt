package `in`.koreatech.koin.domain.state.signup

sealed class SignupContinuationState {
    object RequestedEmailValidation: SignupContinuationState()
    object CheckNickName: SignupContinuationState() // 닉네임 중복버튼 눌렀는지 확인
    object CheckGender: SignupContinuationState() // 성별 라디오 버튼 눌렀는지 확인
    object CheckGraduate: SignupContinuationState() // 졸업생 라디오 버튼을 눌렀는지 확인
    object InitName: SignupContinuationState() // 이름을 작성했는지 확인

    object InitNickName: SignupContinuationState()
    object InitPhoneNumber: SignupContinuationState() // 전화번호를 작성했는지 확인
    object InitStudentId: SignupContinuationState() // 학번을 작성했는지 확인
    object InitMajor: SignupContinuationState() // 전공을 선택했는지 확인


    object EmailIsNotValidate: SignupContinuationState()
    object PasswordIsNotValidate: SignupContinuationState()
    object PasswordNotMatching: SignupContinuationState()
    object NotAgreedPrivacyTerms: SignupContinuationState()
    object NotAgreedKoinTerms: SignupContinuationState()
    object CheckComplete: SignupContinuationState()
}
