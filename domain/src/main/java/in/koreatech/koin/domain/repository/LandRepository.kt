package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.land.Land
import `in`.koreatech.koin.domain.model.land.LandDetail

interface LandRepository {
    suspend fun getLandList(): List<Land>
    suspend fun getLandDetail(id: Int): LandDetail
}