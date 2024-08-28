package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.httpExceptionMapper
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordRequest
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordSmsRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.request.owner.VerificationCodeSmsRequest
import `in`.koreatech.koin.data.request.owner.VerificationSmsRequest
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import javax.inject.Inject

class OwnerChangePasswordRepositoryImpl @Inject constructor(
    private val ownerRemoteDataSource: OwnerRemoteDataSource,
): OwnerChangePasswordRepository {
    override suspend fun requestEmailVerification(
        email: String
    ): Result<Unit> {
        return try {
            ownerRemoteDataSource.changePasswordVerificationEmail (
                OwnerVerificationEmailRequest(
                    address = email
                )
            )
            Result.success(Unit)
        }
        catch (e: HttpException) {
            e.httpExceptionMapper()
        }
        catch (t: CancellationException) {
            throw t
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun authenticateCode(
        email: String,
        authCode: String
    ): Result<Unit> {
        return try {
            ownerRemoteDataSource.changePasswordVerificationCode (
                OwnerVerificationCodeRequest(
                    address = email,
                    certificationCode = authCode
                )
            )

            Result.success(Unit)
        }
        catch (e: HttpException) {
            e.httpExceptionMapper()
        }
        catch (t: CancellationException) {
            throw t
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changePassword(
        email: String, password: String
    ): Result<Unit> {
        return try {
            ownerRemoteDataSource.ownerChangePassword (
                OwnerChangePasswordRequest(
                    address = email,
                    password = password
                )
            )
            Result.success(Unit)
        }
        catch (e: HttpException) {
            e.httpExceptionMapper()
        }
        catch (t: CancellationException) {
            throw t
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun requestSmsVerification(phoneNumber: String) {
        return try {
            ownerRemoteDataSource.changePasswordVerificationSms (
                VerificationSmsRequest(
                    phoneNumber = phoneNumber
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            if (e.code()==400)
                throw OwnerError.NotValidPhoneNumberException
            else if (e.code()==404)
                throw OwnerError.NotExistsPhoneNumberException
            else throw e
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun authenticateSmsCode(phoneNumber: String, authCode: String): Result<Unit> {
        return try {
            ownerRemoteDataSource.changePasswordVerificationSmsCode (
                VerificationCodeSmsRequest(
                    phoneNumber = phoneNumber,
                    certificationCode = authCode
                )
            )

            Result.success(Unit)
        }
        catch (e: HttpException) {
            e.httpExceptionMapper()
        }
        catch (t: CancellationException) {
            throw t
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changePasswordSms(phoneNumber: String, password: String): Result<Unit> {
        return try {
            ownerRemoteDataSource.ownerChangePasswordSms (
                OwnerChangePasswordSmsRequest(
                    phoneNumber = phoneNumber,
                    password = password
                )
            )
            Result.success(Unit)
        }
        catch (e: HttpException) {
            e.httpExceptionMapper()
        }
        catch (t: CancellationException) {
            throw t
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}