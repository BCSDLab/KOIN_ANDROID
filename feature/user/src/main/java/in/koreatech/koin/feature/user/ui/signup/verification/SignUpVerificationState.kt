package `in`.koreatech.koin.feature.user.ui.signup.verification

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.feature.user.model.VerificationMethod
import `in`.koreatech.koin.feature.user.model.VerificationCode
import `in`.koreatech.koin.domain.util.ext.isEnglish
import `in`.koreatech.koin.domain.util.ext.isKorean

data class SignUpVerificationState(
    val name: String = "",
    val gender: Gender = Gender.Unknown,
    val phoneNumber: String = "",
    val phoneNumberState: VerificationMethod = VerificationMethod.None,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCode = VerificationCode.None,
    val verificationTimeLeft: Int = 180
)

val SignUpVerificationState.isNameValid: Boolean
    get() = if (name.isKorean()) {
        name.length in 2..5
    } else if (name.isEnglish()) {
        name.length in 2..30
    } else {
        false
    }

val SignUpVerificationState.currentStep: SignUpVerificationStep
    get() = when {
        phoneNumber.isNotEmpty() && phoneNumberState is VerificationMethod.Sent -> SignUpVerificationStep.VERIFICATION_CODE
        name.isNotEmpty() && isNameValid && gender != Gender.Unknown -> SignUpVerificationStep.PHONE_NUMBER
        else -> SignUpVerificationStep.INITIAL
    }

val SignUpVerificationState.enabled: Boolean
    get() = verificationCodeState is VerificationCode.Valid && name.isNotBlank() && isNameValid && gender != Gender.Unknown && phoneNumber.isNotBlank()

enum class SignUpVerificationStep {
    INITIAL,
    PHONE_NUMBER,
    VERIFICATION_CODE
}
