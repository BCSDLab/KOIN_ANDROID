package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toFileUrlList
import `in`.koreatech.koin.data.request.owner.OwnerRegisterRequest
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.model.owner.OwnerRegisterUrl
import `in`.koreatech.koin.domain.repository.OwnerRegisterRepository
import retrofit2.HttpException
import java.io.EOFException

class OwnerRegisterRepositoryImpl(
    private val ownerRemoteDataSource: OwnerRemoteDataSource
): OwnerRegisterRepository {
    override suspend fun ownerRegister(
        attachments: List<OwnerRegisterUrl>,
        companyNumber: String,
        email: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: Int?,
        shopName: String
    ): Result<Unit> {
        return try {
            ownerRemoteDataSource.postOwnerRegister(
                OwnerRegisterRequest(
                    attachments.toFileUrlList(), companyNumber, email, name, password, phoneNumber, shopId, shopName
                )
            )

            Result.success(Unit)
        } catch (e: EOFException) {
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (t: Throwable) {
                Result.failure(t)
        }
    }
}