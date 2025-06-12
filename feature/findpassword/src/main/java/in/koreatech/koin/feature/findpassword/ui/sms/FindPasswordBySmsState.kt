package `in`.koreatech.koin.feature.findpassword.ui.sms

import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode

data class FindPasswordBySmsState(
    val isLoading: Boolean = false,
    val loginId: String = "",
    val phoneNumber: String = "",
    val phoneNumberState: PhoneNumber = PhoneNumber.None,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCode = VerificationCode.None,
    val verificationTimeLeft: Int = 180
)
