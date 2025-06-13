package `in`.koreatech.koin.feature.findpassword.ui.verification

import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode

data class FindPasswordByVerificationState(
    val isLoading: Boolean = false,
    val loginId: String = "",
    val loginIdValid: Boolean = true,
    val phoneNumber: String = "",
    val phoneNumberState: PhoneNumber = PhoneNumber.None,
    val isSms: Boolean = true,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCode = VerificationCode.None,
    val verificationTimeLeft: Int = 180
)
