package `in`.koreatech.koin.feature.user.ui.signup.verification

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.util.ext.isNameFormat
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState

data class SignUpVerificationState(
    val name: String = "",
    val gender: Gender = Gender.Unknown,
    val phoneNumber: String = "",
    val phoneNumberState: VerificationMethodState = VerificationMethodState.None,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCodeState = VerificationCodeState.None,
    val verificationTimeLeft: Int = 180
)

val SignUpVerificationState.isNameValid: Boolean
    get() = name.isNameFormat()

val SignUpVerificationState.currentStep: SignUpVerificationStep
    get() = when {
        phoneNumber.isNotEmpty() && phoneNumberState is VerificationMethodState.Sent -> SignUpVerificationStep.VERIFICATION_CODE
        name.isNotEmpty() && isNameValid && gender != Gender.Unknown -> SignUpVerificationStep.PHONE_NUMBER
        else -> SignUpVerificationStep.INITIAL
    }

val SignUpVerificationState.enabled: Boolean
    get() = verificationCodeState is VerificationCodeState.Valid && name.isNotBlank() && isNameValid && gender != Gender.Unknown && phoneNumber.isNotBlank()

enum class SignUpVerificationStep {
    INITIAL,
    PHONE_NUMBER,
    VERIFICATION_CODE
}
