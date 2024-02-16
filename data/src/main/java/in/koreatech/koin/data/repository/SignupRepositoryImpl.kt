package `in`.koreatech.koin.data.repository

import android.util.Log
import `in`.koreatech.koin.data.mapper.toGraduate
import `in`.koreatech.koin.data.mapper.toPhoneNumber
import `in`.koreatech.koin.data.mapper.toShcoolEamil
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.response.user.StudentInfoResponse
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
            Log.d("로그", "성공")
            userRemoteDataSource.sendRegisterEmail(
                StudentInfoResponse(
                    email = portalAccount.toShcoolEamil(),
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
            Log.d("로그 e", e.toString())
            if (e.code() == 409) Result.failure(SignupAlreadySentEmailException())
            else if (e.code() == 201) Result.success(Unit)
            else Result.failure(e)
        } catch (t: Throwable) {
            Log.d("로그 t", t.toString())
            Result.failure(t)
        }
    }
}