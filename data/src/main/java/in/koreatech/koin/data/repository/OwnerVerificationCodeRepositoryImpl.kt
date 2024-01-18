package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toAuthToken
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.error.owner.IncorrectVerificationCodeException
import `in`.koreatech.koin.domain.error.owner.OverDueTimeException
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.model.owner.OwnerAuthToken
import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import retrofit2.HttpException
import javax.inject.Inject

class OwnerVerificationCodeRepositoryImpl @Inject constructor(
    private val ownerRemoteDataSource: OwnerRemoteDataSource
):OwnerVerificationCodeRepository {
    override suspend fun compareVerificationCode(
        address: String,
        verificationCode: String
    ): Pair<OwnerAuthToken?, Result<Unit>> {
        return try {
            val tempToken = ownerRemoteDataSource.postVerificationCode(
                OwnerVerificationCodeRequest(
                    address = address,
                    certificationCode = verificationCode
                )
            )

            return Pair(tempToken.toAuthToken(), Result.success(Unit))
        } catch (e: HttpException) {
            when(e.code()) {
                409 -> Pair(null, Result.failure(SignupAlreadySentEmailException()))
                410 -> Pair(null, Result.failure(OverDueTimeException()))
                422 -> Pair(null, Result.failure(IncorrectVerificationCodeException()))
                else -> Pair(null, Result.failure(e))
            }
        } catch (t: Throwable) {
            Pair(null, Result.failure(t))
        }
    }

}