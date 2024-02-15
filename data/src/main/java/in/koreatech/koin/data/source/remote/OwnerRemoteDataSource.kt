package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.OwnerApi
import `in`.koreatech.koin.data.response.owner.OwnerResponse
import javax.inject.Inject

class OwnerRemoteDataSource @Inject constructor(
    private val ownerApi: OwnerApi
) {
    suspend fun getOwnerName(): OwnerResponse {
        return ownerApi.getOwnerInfo()
    }
}