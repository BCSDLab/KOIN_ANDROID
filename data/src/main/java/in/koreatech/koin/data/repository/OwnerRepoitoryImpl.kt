package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toOwnerName
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.repository.OwnerRepository

import javax.inject.Inject

class OwnerRepoitoryImpl @Inject constructor(
    private val ownerRemoteDataSource: OwnerRemoteDataSource
) : OwnerRepository {

    override suspend fun getOwnerName(): String {
        val response = ownerRemoteDataSource.getOwnerName()
        return response.toOwnerName()
    }

}