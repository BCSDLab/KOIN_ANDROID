package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toLand
import `in`.koreatech.koin.data.mapper.toLandDetail
import `in`.koreatech.koin.data.source.remote.LandRemoteDataSource
import `in`.koreatech.koin.domain.model.land.Land
import `in`.koreatech.koin.domain.model.land.LandDetail
import `in`.koreatech.koin.domain.repository.LandRepository
import javax.inject.Inject

class LandRepositoryImpl @Inject constructor(
    private val landRemoteDataSource: LandRemoteDataSource
) : LandRepository {
    override suspend fun getLandList(): List<Land> {
        return landRemoteDataSource.getLandList().lands.map {
            it.toLand()
        }
    }

    override suspend fun getLandDetail(id: Int): LandDetail {
        return landRemoteDataSource.getLandDetail(id).toLandDetail()
    }
}