package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNameFormat
import javax.inject.Inject

class SignupRequestEmailVerificationUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(
        portalAccount: String,
        gender: Gender?,
        isGraduated: Graduated?,
        major: String?,
        name: String?,
        nickName: String?,
        password: String,
        phoneNumber: String?,
        studentNumber: String?,
        isCheckNickname: Boolean
    ): Result<SignupContinuationState> {

        if (!name.isNullOrBlank() && !name.isNameFormat())
            return Result.success(SignupContinuationState.CheckNameFormat)
        else if (!phoneNumber.isNullOrBlank() && (phoneNumber.length != 11))
            return Result.success(SignupContinuationState.CheckPhoneNumberFormat)
        else if (!nickName.isNullOrBlank() && !isCheckNickname)
            return Result.success(SignupContinuationState.CheckNickName)

        return signupRepository.requestEmailVerification(
            portalAccount = portalAccount,
            gender = gender,
            isGraduated = isGraduated,
            major = if (major.isNullOrBlank()) null else major,
            name = if (name.isNullOrBlank()) null else name,
            nickName = if (nickName.isNullOrBlank()) null else nickName,
            password = password,
            phoneNumber = if (phoneNumber.isNullOrBlank()) null else phoneNumber,
            studentNumber = if (studentNumber.isNullOrBlank()) null else studentNumber,
        ).map {
            SignupContinuationState.RequestedEmailValidation
        }
    }
}