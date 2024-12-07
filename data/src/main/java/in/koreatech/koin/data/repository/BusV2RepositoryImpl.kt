package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.BusV2RemoteDataSource
import `in`.koreatech.koin.domain.model.bus.v2.BusNotice
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetable
import `in`.koreatech.koin.domain.repository.BusV2Repository
import javax.inject.Inject

class BusV2RepositoryImpl @Inject constructor(
    private val busRemoteDataSource: BusV2RemoteDataSource
) : BusV2Repository {

    override suspend fun fetchBusNotice(): Result<BusNotice> {
        return runCatching {
            busRemoteDataSource.fetchBusNotice().toBusNotice()
        }
    }

    override suspend fun fetchShuttleTimetable(id: String): Result<ShuttleTimetable> {
        return runCatching {
            busRemoteDataSource.fetchShuttleTimetable(id).toShuttleTimetable()
        }
    }
}