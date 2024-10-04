package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toBoolean
import `in`.koreatech.koin.data.mapper.toInt
import `in`.koreatech.koin.data.mapper.toPhoneNumber
import `in`.koreatech.koin.data.mapper.toTerm
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.data.util.getErrorResponse
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.model.term.Term
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import retrofit2.HttpException
import javax.inject.Inject

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

    override suspend fun getPrivacyTerm(): Term {
        return signupTermsLocalDataSource.getPrivacyTerm().toTerm()
    }

    override suspend fun getKoinTerm(): Term {
        return signupTermsLocalDataSource.getKoinTerms().toTerm()
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
        studentNumber: String?,
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
            if (e.code() == 409) Result.failure(SignupAlreadySentEmailException())
            else Result.failure(Throwable(e.getErrorResponse()?.message ?: ""))
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}