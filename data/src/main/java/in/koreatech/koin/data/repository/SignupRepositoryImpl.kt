package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toBoolean
import `in`.koreatech.koin.data.mapper.toInt
import `in`.koreatech.koin.data.mapper.toPhoneNumber
import `in`.koreatech.koin.data.mapper.toTerm
import `in`.koreatech.koin.data.request.user.GeneralInfoRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequestV2
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.data.util.getErrorResponse
import `in`.koreatech.koin.data.util.toKoinUnknownErrorException
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.error.user.KoinUserException
import `in`.koreatech.koin.domain.model.term.Term
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.repository.SignupRepository
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

    override suspend fun isUsernameDuplicatedV2(nickname: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.checkNicknameV2(nickname)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            409 -> KoinUserException.NicknameConflictException()
                            400 -> KoinUserException.NicknameInvalidException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun isPhoneDuplicated(phone: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.checkPhoneNumberDuplicate(phone)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            409 -> KoinUserException.PhoneNumberConflictException()
                            400 -> KoinUserException.PhoneNumberInvalidException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun postStudentRegister(
        name: String,
        phoneNumber: String,
        loginId: String,
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
                    loginId = loginId,
                    password = password.toSHA256(),
                    department = department,
                    studentNumber = studentNumber,
                    gender = gender,
                    email = email.ifBlank { null },
                    nickname = nickname.ifBlank { null }
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }

    override suspend fun isLoginIdDuplicated(loginId: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.checkLoginId(loginId)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            409 -> KoinUserException.LoginIdConflictException()
                            400 -> KoinUserException.LoginIdInvalidException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun isEmailDuplicated(email: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.checkEmail(email)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            409 -> KoinUserException.EmailConflictException()
                            400 -> KoinUserException.EmailInvalidException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun postGeneralRegister(
        name: String,
        phoneNumber: String,
        loginId: String,
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
                    loginId = loginId,
                    password = password.toSHA256(),
                    gender = gender,
                    email = email.ifBlank { null },
                    nickname = nickname.ifBlank { null }
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }
}
