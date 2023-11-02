package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.request.owner.OwnerSignUpRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.error.signup.InCorrectEmailAddressException
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import android.util.Log
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
        email: String
    ): Result<Unit> {
        return try {
            val response = ownerRemoteDataSource.postVerificationEmail (
                OwnerVerificationEmailRequest(
                    address = email
                )
            )

            Result.success(Unit)
        } catch (e: HttpException) {
            if(e.code() == 409) Result.failure(SignupAlreadySentEmailException())
            else if(e.code() == 422) Result.failure(InCorrectEmailAddressException())
            else Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}