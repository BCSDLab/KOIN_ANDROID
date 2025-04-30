package `in`.koreatech.koin.feature.signup.ui.verification

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

data class SignUpVerificationState(
    val name: String = "",
    val gender: Gender = Gender.Unknown,
    val phoneNumber: String = "",
    val phoneNumberState: SignupContinuationState? = null,
    val verificationCode: String = "",
    val verificationCodeState: SignupContinuationState? = null,
    val verificationTimeLeft: Int = 180
)

val SignUpVerificationState.currentStep: SignUpVerificationStep
    get() = when {
        phoneNumber.isNotEmpty() && phoneNumberState is SignupContinuationState.RequestedSmsValidationWithRemainingCount -> SignUpVerificationStep.VERIFICATION_CODE
        name.isNotEmpty() && gender != Gender.Unknown -> SignUpVerificationStep.PHONE_NUMBER
        else -> SignUpVerificationStep.INITIAL
    }

enum class SignUpVerificationStep {
    INITIAL,
    PHONE_NUMBER,
    VERIFICATION_CODE
}
