package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.safeApiCall
import `in`.koreatech.koin.data.mapper.toAuthToken
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.VerificationCodeSmsRequest
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.model.owner.OwnerAuthToken
import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import retrofit2.HttpException
import javax.inject.Inject

class OwnerVerificationCodeRepositoryImpl @Inject constructor(
    private val ownerRemoteDataSource: OwnerRemoteDataSource
) : OwnerVerificationCodeRepository {
    override suspend fun compareVerificationCode(
        address: String,
        verificationCode: String
    ): Result<OwnerAuthToken?> {
        return try {
            val tempToken = ownerRemoteDataSource.postVerificationCode(
                OwnerVerificationCodeRequest(
                    address = address,
                    certificationCode = verificationCode
                )
            )

            Result.success(tempToken.toAuthToken())// Pair(tempToken.toAuthToken(), Result.success(Unit))
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun verifySmsCode(
        phoneNumber: String,
        verificationCode: String
    ): Result<OwnerAuthToken?> {
        return safeApiCall {
            val tempToken = ownerRemoteDataSource.postVerificationCodeSms(
                VerificationCodeSmsRequest(
                    phoneNumber = phoneNumber,
                    certificationCode = verificationCode
                )
            )
            tempToken.toAuthToken()
        }
    }
}