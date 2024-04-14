package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.formatPhoneNumber
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class SignupRequestEmailVerificationUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(
            portalAccount: String,
            gender: Gender?,
            isGraduated: Graduated?,
            major: String,
            name:String,
            nickName: String,
            password: String,
            phoneNumber: String,
            studentNumber: String,
            isCheckNickname: Boolean
    ): Result<SignupContinuationState> {
        return when {
            name.isEmpty() -> Result.success(SignupContinuationState.InitName)
            !isCheckNickname -> Result.success(SignupContinuationState.CheckNickName)
            gender == null -> Result.success(SignupContinuationState.CheckGender)
            phoneNumber.isEmpty() || (phoneNumber.length != 11) -> Result.success(SignupContinuationState.InitPhoneNumber)
            studentNumber.isEmpty() -> Result.success(SignupContinuationState.InitStudentId)
            major.isEmpty() -> Result.success(SignupContinuationState.CheckDept)
            isGraduated == null -> Result.success(SignupContinuationState.CheckGraduate)
            else -> signupRepository.requestEmailVerification(
                portalAccount = portalAccount,
                gender = gender,
                isGraduated = isGraduated,
                major = major,
                name = name,
                nickName = nickName,
                password = password,
                phoneNumber = phoneNumber.formatPhoneNumber(),
                studentNumber = studentNumber,
            ).map {
                SignupContinuationState.RequestedEmailValidation
            }
        }
    }
}