package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class SignupRequestEmailVerificationUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(
        portalAccount: String,
        gender: Int,
        isGraduated: Int,
        major: String,
        name:String,
        nickName: String,
        password: String,
        phoneNumber: String,
        studentNumber: String,
        isCheckNickname: Boolean
    ): Result<SignupContinuationState> {
        return when {
            name == "" -> Result.success(SignupContinuationState.InitName)
            !isCheckNickname -> Result.success(SignupContinuationState.CheckNickName)
            gender == 2 -> Result.success(SignupContinuationState.CheckGender)
            phoneNumber == "" -> Result.success(SignupContinuationState.InitPhoneNumber)
            studentNumber == "" -> Result.success(SignupContinuationState.InitStudentId)
            isGraduated == 2 -> Result.success(SignupContinuationState.CheckGraduate)
            else -> signupRepository.requestEmailVerification(
                email = portalAccount,
                password = password.toSHA256()
            ).map {
                SignupContinuationState.RequestedEmailValidation
            }
        }
    }
}