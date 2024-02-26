package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toGraduate
import `in`.koreatech.koin.data.mapper.toPhoneNumber
import `in`.koreatech.koin.data.mapper.toSchoolEamil
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.repository.SignupRepository
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

    override suspend fun requestEmailVerification(
        portalAccount: String,
        gender: Int,
        isGraduated: Int,
        major: String,
        name:String,
        nickName: String,
        password: String,
        phoneNumber: String,
        studentNumber: String,
    ): Result<Unit> {
        return try {
            userRemoteDataSource.sendRegisterEmail(
                StudentInfoRequest(
                    email = portalAccount.toSchoolEamil(),
                    gender = gender,
                    isGraduated = isGraduated.toGraduate(),
                    major = major,
                    name = name,
                    nickName = nickName,
                    password = password,
                    phoneNumber = phoneNumber.toPhoneNumber(),
                    studentNumber = studentNumber
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            if (e.code() == 409) Result.failure(SignupAlreadySentEmailException())
            else Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}