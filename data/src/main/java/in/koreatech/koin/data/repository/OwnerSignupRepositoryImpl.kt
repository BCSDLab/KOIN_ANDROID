package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.httpExceptionMapper
import `in`.koreatech.koin.data.mapper.safeApiCall
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.request.owner.VerificationSmsRequest
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class OwnerSignupRepositoryImpl @Inject constructor(
    private val ownerRemoteDataSource: OwnerRemoteDataSource,
    private val signupTermsLocalDataSource: SignupTermsLocalDataSource
) : OwnerSignupRepository {
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
            ownerRemoteDataSource.postVerificationEmail(
                OwnerVerificationEmailRequest(
                    address = email
                )
            )

            Result.success(Unit)
        } catch (e: HttpException) {
            e.httpExceptionMapper()
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun requestSmsVerificationCode(
        phoneNumber: String
    ): Result<Unit> {
        return safeApiCall {
            ownerRemoteDataSource.postVerificationSms(
                VerificationSmsRequest(
                    phoneNumber = phoneNumber
                )
            )
        }
    }


    override suspend fun getExistsAccount(phoneNumber: String): Pair<Boolean, String?> {
        return try {
            ownerRemoteDataSource.checkExistsAccount(phoneNumber)
            Pair(false, null)
        } catch (e: HttpException) {
            if (e.code() == 409 || e.code() == 400) {
                val errorBody = e.response()?.errorBody()?.string()
                val message = JSONObject(errorBody).getString("message")
                Pair(true, message)
            } else {
                Pair(true, null)
            }

        }

    }
}
