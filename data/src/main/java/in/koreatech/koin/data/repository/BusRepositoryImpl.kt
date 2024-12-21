package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.BusLocalDataSource
import `in`.koreatech.koin.data.source.remote.BusRemoteDataSource
import `in`.koreatech.koin.domain.model.bus.BusNotice
import `in`.koreatech.koin.domain.model.bus.BusSearchResult
import `in`.koreatech.koin.domain.model.bus.CityTimetable
import `in`.koreatech.koin.domain.model.bus.ExpressTimetable
import `in`.koreatech.koin.domain.model.bus.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.ShuttleTimetable
import `in`.koreatech.koin.domain.repository.BusRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BusRepositoryImpl @Inject constructor(
    private val busRemoteDataSource: BusRemoteDataSource,
    private val busLocalDataSource: BusLocalDataSource
) : BusRepository {

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

    override suspend fun fetchBusSearchResult(
        date: LocalDate,
        time: LocalTime,
        busType: String,
        departure: String,
        arrival: String
    ): Result<List<BusSearchResult>> {
        return runCatching {
            busRemoteDataSource.fetchBusSearchResult(
                date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date),
                time = DateTimeFormatter.ofPattern("HH:mm").format(time),
                busType = busType,
                departure = departure,
                arrival = arrival
            ).schedules?.map { it.toBusSearchResult() }.orEmpty()
        }
    }

    override suspend fun getLastShownNoticeId(): Result<Int> {
        return runCatching {
            busLocalDataSource.getLastShownNoticeId()
        }
    }

    override suspend fun saveLastShownNoticeId(id: Int): Result<Unit> {
        return runCatching {
            busLocalDataSource.saveLastShownNoticeId(id)
        }
    }
}