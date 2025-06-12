package `in`.koreatech.koin.feature.findpassword.ui.email

import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode

data class FindPasswordByEmailState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailState: PhoneNumber = PhoneNumber.None,
    val verificationCode: String = "",
    val verificationCodeState: VerificationCode = VerificationCode.None,
    val verificationTimeLeft: Int = 300
)
