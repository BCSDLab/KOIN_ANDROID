package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.request.owner.OwnerLoginRequest
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import retrofit2.HttpException
import javax.inject.Inject

class OwnerSignupRepositoryImpl @Inject constructor(
    private val ownerRemoteDataSource: OwnerRemoteDataSource,
    private val signupTermsLocalDataSource: SignupTermsLocalDataSource
): OwnerSignupRepository {
    override suspend fun getPrivacyTermText(): String {
        return signupTermsLocalDataSource.getPrivacyTermText()
    }

    override suspend fun getKoinTermText(): String {
        return signupTermsLocalDataSource.getKoinTermText()
    }

    override suspend fun requestEmailVerification(
        email: String,
        hashedPassword: String
    ): Result<Unit> {
        return try {
            ownerRemoteDataSource.sendRegisterEmail(
                OwnerLoginRequest(
                    email = email,
                    passwordHashed = hashedPassword
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            if(e.code() == 409) Result.failure(SignupAlreadySentEmailException())
            else Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}