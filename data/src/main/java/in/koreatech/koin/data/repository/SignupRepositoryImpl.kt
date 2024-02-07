package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.request.user.LoginRequest
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
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            userRemoteDataSource.sendRegisterEmail(
                LoginRequest(
                    email = email,
                    password = password
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