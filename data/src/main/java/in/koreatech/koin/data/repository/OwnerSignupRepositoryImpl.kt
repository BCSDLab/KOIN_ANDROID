package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.httpExceptionMapper
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.request.owner.VerificationSmsRequest
import `in`.koreatech.koin.data.response.owner.CheckCompanyNumberResponse
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import javax.inject.Inject
import retrofit2.HttpException

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

    override suspend fun requestEmailVerification(email: String): Result<Unit> {
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

    override suspend fun requestSmsVerificationCode(phoneNumber: String): Result<Unit> {
        return try {
            ownerRemoteDataSource.postVerificationSms(
                VerificationSmsRequest(
                    phoneNumber = phoneNumber
                )
            )
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun getExistsAccount(phoneNumber: String): Result<Unit> {
        return try {
            ownerRemoteDataSource.checkExistsAccount(phoneNumber)
            Result.success(Unit)
        } catch (e: HttpException) {
            if (e.code() == 400) {
                throw OwnerError.NotValidPhoneNumberException
            } else if (e.code() == 409) {
                throw OwnerError.ExistsPhoneNumberException
            } else {
                throw e
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun checkExistsCompanyNumber(companyNumber: String) {
        return try {
            ownerRemoteDataSource.checkExistsCompanyNumber(CheckCompanyNumberResponse(companyNumber))
        } catch (e: HttpException) {
            if (e.code() == 409) {
                throw OwnerError.CompanyNumberIsDuplicatedException
            } else {
                throw e
            }
        } catch (t: Throwable) {
            throw t
        }
    }
}
