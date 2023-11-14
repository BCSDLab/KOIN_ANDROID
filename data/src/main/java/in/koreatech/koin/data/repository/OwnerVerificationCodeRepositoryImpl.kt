package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.error.owner.IncorrectVerificationCodeException
import `in`.koreatech.koin.domain.error.owner.OverDueTimeException
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import retrofit2.HttpException
import javax.inject.Inject

class OwnerVerificationCodeRepositoryImpl @Inject constructor(
    private val ownerRemoteDataSource: OwnerRemoteDataSource
):OwnerVerificationCodeRepository {
    override suspend fun compareVerificationCode(
        address: String,
        verificationCode: String
    ): Result<Unit> {
        return try {
            ownerRemoteDataSource.postVerificationCode(
                OwnerVerificationCodeRequest(
                    address = address,
                    certificationCode = verificationCode
                )
            )

            Result.success(Unit)
        } catch (e: HttpException) {
            if(e.code() == 409) Result.failure(SignupAlreadySentEmailException())
            else if(e.code() == 410) Result.failure(OverDueTimeException())
            else if(e.code() == 422) Result.failure(IncorrectVerificationCodeException())
            else Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}