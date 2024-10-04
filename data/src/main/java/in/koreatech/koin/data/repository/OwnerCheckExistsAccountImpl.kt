package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.repository.OwnerCheckExistsAccountRepository
import retrofit2.HttpException

class OwnerCheckExistsAccountImpl(
    private val ownerRemoteDataSource: OwnerRemoteDataSource
) : OwnerCheckExistsAccountRepository {
    override suspend fun checkExistsAccount(phoneNumber: String): Boolean {
        return try {
            ownerRemoteDataSource.checkExistsAccount(phoneNumber)
            false
        } catch (e: HttpException) {
            if (e.code() == 409) true
            else throw e
        }

    }
}