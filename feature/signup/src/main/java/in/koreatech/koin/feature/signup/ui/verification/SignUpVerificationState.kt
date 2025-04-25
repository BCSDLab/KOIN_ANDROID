package `in`.koreatech.koin.feature.signup.ui.verification

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

data class SignUpVerificationState(
    val step: SignUpVerificationStep = SignUpVerificationStep.INITIAL,
    val name: String = "",
    val gender: Gender = Gender.Unknown,
    val phoneNumber: String = "",
    val phoneNumberState: SignupContinuationState? = null,
    val verificationCode: String = "",
    val verificationCodeState: SignupContinuationState? = null,
    val verificationTimeLeft: Int = 180
)

enum class SignUpVerificationStep {
    INITIAL,
    PHONE_NUMBER,
    VERIFICATION_CODE
}
