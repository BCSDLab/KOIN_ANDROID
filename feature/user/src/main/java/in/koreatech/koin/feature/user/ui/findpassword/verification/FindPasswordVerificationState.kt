package `in`.koreatech.koin.feature.user.ui.findpassword.verification

import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode

data class FindPasswordVerificationState(
    val isLoading: Boolean = false,
    val loginId: String = "",
    val loginIdValid: Boolean = true,
    val verificationMethod: String = "",
    val verificationMethodState: PhoneNumber = PhoneNumber.None,
    val isSms: Boolean = true,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCode = VerificationCode.None,
    val verificationTimeLeft: Int = 180
)
