package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toBoolean
import `in`.koreatech.koin.data.mapper.toInt
import `in`.koreatech.koin.data.mapper.toPhoneNumber
import `in`.koreatech.koin.data.mapper.toTerm
import `in`.koreatech.koin.data.request.user.GeneralInfoRequest
import `in`.koreatech.koin.data.request.user.SmsSendRequest
import `in`.koreatech.koin.data.request.user.SmsVerifyRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequestV2
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.data.util.getErrorResponse
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.model.term.Term
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject
import retrofit2.HttpException

class SignupRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val signupTermsLocalDataSource: SignupTermsLocalDataSource
) : SignupRepository {
    override suspend fun getPrivacyTermText(): String {
        return signupTermsLocalDataSource.getPrivacyTermText()
    }

    override suspend fun getKoinTermText(): String {
        return signupTermsLocalDataSource.getKoinTermText()
    }

    override suspend fun getMarketingTermText(): String {
        return signupTermsLocalDataSource.getMarketingTermText()
    }

    override suspend fun getPrivacyTerm(): Term {
        return signupTermsLocalDataSource.getPrivacyTerm().toTerm()
    }

    override suspend fun getKoinTerm(): Term {
        return signupTermsLocalDataSource.getKoinTerms().toTerm()
    }

    override suspend fun getMarketingTerm(): Term {
        return signupTermsLocalDataSource.getMarketingTerms().toTerm()
    }

    override suspend fun requestEmailVerification(
        portalAccount: String,
        gender: Gender,
        isGraduated: Graduated?,
        major: String?,
        name: String?,
        nickName: String?,
        password: String,
        phoneNumber: String?,
        studentNumber: String?
    ): Result<Unit> {
        return try {
            userRemoteDataSource.sendRegisterEmail(
                StudentInfoRequest(
                    email = portalAccount,
                    gender = gender.toInt(),
                    isGraduated = isGraduated?.toBoolean(),
                    major = major,
                    name = name,
                    nickName = nickName,
                    password = password.toSHA256(),
                    phoneNumber = phoneNumber?.toPhoneNumber(),
                    studentNumber = studentNumber
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            if (e.code() == 409) {
                Result.failure(SignupAlreadySentEmailException())
            } else {
                Result.failure(Throwable(e.getErrorResponse()?.message ?: ""))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun isUsernameDuplicatedV2(nickname: String): SignupContinuationState {
        return try {
            userRemoteDataSource.checkNicknameV2(nickname)
            SignupContinuationState.AvailableNickname
        } catch (e: HttpException) {
            when (e.code()) {
                409 -> SignupContinuationState.NicknameDuplicated
                else -> SignupContinuationState.Failed(
                    message = e.getErrorResponse().message ?: "",
                    throwable = e
                )
            }
        }
    }

    override suspend fun isPhoneDuplicated(phone: String): SignupContinuationState {
        return try {
            userRemoteDataSource.checkPhoneNumberDuplicate(phone)
            SignupContinuationState.AvailablePhoneNumber
        } catch (e: HttpException) {
            when (e.code()) {
                409 -> SignupContinuationState.PhoneNumberDuplicated
                400 -> SignupContinuationState.CheckPhoneNumberFormat
                else -> SignupContinuationState.Failed(
                    message = e.getErrorResponse().message ?: "",
                    throwable = e
                )
            }
        }
    }

    override suspend fun postStudentRegister(
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        department: String,
        studentNumber: String,
        gender: String,
        email: String,
        nickname: String
    ): Result<Unit> {
        return try {
            userRemoteDataSource.postStudentRegister(
                StudentInfoRequestV2(
                    name = name,
                    phoneNumber = phoneNumber,
                    userId = userId,
                    password = password,
                    department = department,
                    studentNumber = studentNumber,
                    gender = gender,
                    email = email,
                    nickname = nickname
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }

    override suspend fun isUserIdDuplicated(userId: String): SignupContinuationState {
        return try {
            userRemoteDataSource.checkUserId(userId)
            SignupContinuationState.AvailableUserId
        } catch (e: HttpException) {
            when (e.code()) {
                409 -> SignupContinuationState.UserIdDuplicated
                400 -> SignupContinuationState.CheckUserIdFormat
                else -> SignupContinuationState.Failed(
                    message = e.getErrorResponse().message ?: "",
                    throwable = e
                )
            }
        }
    }

    override suspend fun postGeneralRegister(
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        gender: String,
        email: String,
        nickname: String
    ): Result<Unit> {
        return try {
            userRemoteDataSource.postGeneralRegister(
                GeneralInfoRequest(
                    name = name,
                    phoneNumber = phoneNumber,
                    userId = userId,
                    password = password,
                    gender = gender,
                    email = email,
                    nickname = nickname
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }

    override suspend fun requestSmsVerification(phoneNumber: String): SignupContinuationState {
        return try {
            userRemoteDataSource.sendSMS(SmsSendRequest(phoneNumber = phoneNumber)).let {
                SignupContinuationState.RequestedSmsValidationWithRemainingCount(
                    totalCount = it.totalCount,
                    remainingCount = it.remainingCount,
                    currentCount = it.currentCount
                )
            }
        } catch (e: HttpException) {
            when (e.code()) {
                429 -> SignupContinuationState.SmsCodeRequestCountIsExceeded
                400 -> SignupContinuationState.CheckPhoneNumberFormat
                else -> SignupContinuationState.Failed(
                    message = e.getErrorResponse().message ?: "",
                    throwable = e
                )
            }
        }
    }

    override suspend fun verifyCertificationCode(phoneNumber: String, verificationCode: String): SignupContinuationState {
        return try {
            userRemoteDataSource.verifyCode(
                SmsVerifyRequest(
                    phoneNumber = phoneNumber,
                    verificationCode = verificationCode
                )
            )
            SignupContinuationState.SmsCodeIsValidated
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> SignupContinuationState.SmsCodeIsExpired
                else -> SignupContinuationState.SmsCodeIsNotValidate
            }
        }
    }
}
