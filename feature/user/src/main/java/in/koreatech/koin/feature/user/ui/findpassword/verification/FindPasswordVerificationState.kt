package `in`.koreatech.koin.feature.user.ui.findpassword.verification

import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState

data class FindPasswordVerificationState(
    val isLoading: Boolean = false,
    val loginId: String = "",
    val loginIdValid: Boolean = true,
    val verificationMethod: String = "",
    val verificationMethodState: VerificationMethodState = VerificationMethodState.None,
    val isSms: Boolean = true,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCodeState = VerificationCodeState.None,
    val verificationTimeLeft: Int = 180
)
