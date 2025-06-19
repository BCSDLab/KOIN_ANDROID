package `in`.koreatech.koin.feature.user.ui.findid.verification

import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode

data class FindIdVerificationState(
    val isLoading: Boolean = false,
    val isSms: Boolean = true,
    val verificationMethod: String = "",
    val verificationMethodState: PhoneNumber = PhoneNumber.None,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCode = VerificationCode.None,
    val verificationTimeLeft: Int = 180
)
