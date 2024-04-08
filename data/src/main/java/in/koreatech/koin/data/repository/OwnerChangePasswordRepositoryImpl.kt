package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.httpExceptionMapper
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
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
        } catch (e: HttpException) {
            e.httpExceptionMapper()
        } catch (t: Throwable) {
            Result.failure(t)
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
        } catch (e: HttpException) {
            e.httpExceptionMapper()
        } catch (t: Throwable) {
            Result.failure(t)
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
        } catch (e: HttpException) {
            e.httpExceptionMapper()
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}