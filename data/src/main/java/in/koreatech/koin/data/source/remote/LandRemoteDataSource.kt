package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.LandApi
import `in`.koreatech.koin.data.response.land.LandDetailResponse
import `in`.koreatech.koin.data.response.land.LandsResponse
import javax.inject.Inject

class LandRemoteDataSource @Inject constructor(
    private val landApi: LandApi
) {
    suspend fun getLandList(): LandsResponse = landApi.getLandList()

    suspend fun getLandDetail(id: Int): LandDetailResponse = landApi.getLandDetail(id)
}