package `in`.koreatech.koin.feature.user.ui.findid.verification

import `in`.koreatech.koin.feature.user.model.VerificationMethodState
import `in`.koreatech.koin.feature.user.model.VerificationCodeState

data class FindIdVerificationState(
    val isLoading: Boolean = false,
    val isSms: Boolean = true,
    val verificationMethod: String = "",
    val verificationMethodState: VerificationMethodState = VerificationMethodState.None,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCodeState = VerificationCodeState.None,
    val verificationTimeLeft: Int = 180
)
