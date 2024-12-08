package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.BusV2RemoteDataSource
import `in`.koreatech.koin.domain.model.bus.v2.BusNotice
import `in`.koreatech.koin.domain.model.bus.v2.CityTimetable
import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetable
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourses
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

    override suspend fun fetchShuttleCourses(): Result<ShuttleCourses> {
        return runCatching {
            busRemoteDataSource.fetchShuttleCourses().toShuttleCourses()
        }
    }

    override suspend fun fetchExpressTimetable(direction: String): Result<ExpressTimetable> {
        return runCatching {
            busRemoteDataSource.fetchExpressTimetable(direction).toExpressTimetable()
        }
    }

    override suspend fun fetchCityTimetable(number: Int, direction: String): Result<CityTimetable> {
        return runCatching {
            busRemoteDataSource.fetchCityTimetable(number, direction).toCityTimetable()
        }
    }
}