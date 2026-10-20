package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.core.network.service.NetworkConnectivityService
import `in`.koreatech.koin.data.source.local.BusLocalDataSource
import `in`.koreatech.koin.data.source.remote.BusRemoteDataSource
import `in`.koreatech.koin.domain.model.bus.BusNotice
import `in`.koreatech.koin.domain.model.bus.BusSearchResult
import `in`.koreatech.koin.domain.model.bus.CityTimetable
import `in`.koreatech.koin.domain.model.bus.ExpressTimetable
import `in`.koreatech.koin.domain.model.bus.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.ShuttleTimetable
import `in`.koreatech.koin.domain.repository.BusRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import timber.log.Timber

class BusRepositoryImpl @Inject constructor(
    private val busRemoteDataSource: BusRemoteDataSource,
    private val busLocalDataSource: BusLocalDataSource,
    private val networkConnectivityService: NetworkConnectivityService
) : BusRepository {
    override suspend fun fetchBusNotice(): Result<BusNotice> {
        return suspendRunCatching {
            fetchAndCache {
                val notice = busRemoteDataSource.fetchBusNotice()
                busLocalDataSource.saveBusNotice(notice)
            }
            busLocalDataSource.getBusNotice().toBusNotice()
        }
    }

    override suspend fun fetchShuttleTimetable(id: String): Result<ShuttleTimetable> {
        return suspendRunCatching {
            fetchAndCache {
                val timetable = busRemoteDataSource.fetchShuttleTimetable(id)
                busLocalDataSource.saveShuttleTimetable(id, timetable)
            }
            busLocalDataSource.getShuttleTimetable(id).toShuttleTimetable()
        }
    }

    override suspend fun fetchShuttleCourses(): Result<ShuttleCourses> {
        return suspendRunCatching {
            fetchAndCache {
                val courses = busRemoteDataSource.fetchShuttleCourses()
                busLocalDataSource.saveShuttleCourses(courses)
            }
            busLocalDataSource.getShuttleCourses().toShuttleCourses()
        }
    }

    override suspend fun fetchExpressTimetable(direction: String): Result<ExpressTimetable> {
        return suspendRunCatching {
            fetchAndCache {
                val timetable = busRemoteDataSource.fetchExpressTimetable(direction)
                busLocalDataSource.saveExpressTimetable(direction, timetable)
            }
            busLocalDataSource.getExpressTimetable(direction).toExpressTimetable()
        }
    }

    override suspend fun fetchCityTimetable(
        number: Int,
        direction: String
    ): Result<CityTimetable> {
        return suspendRunCatching {
            fetchAndCache {
                val timetable = busRemoteDataSource.fetchCityTimetable(number, direction)
                busLocalDataSource.saveCityTimetable(number, direction, timetable)
            }
            busLocalDataSource.getCityTimetable(number, direction).toCityTimetable()
        }
    }

    override suspend fun fetchBusSearchResult(
        date: LocalDate,
        time: LocalTime,
        busType: String,
        departure: String,
        arrival: String
    ): Result<List<BusSearchResult>> {
        return suspendRunCatching {
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
        return suspendRunCatching {
            busLocalDataSource.getLastShownNoticeId()
        }
    }

    override suspend fun saveLastShownNoticeId(id: Int): Result<Unit> {
        return suspendRunCatching {
            busLocalDataSource.saveLastShownNoticeId(id)
        }
    }

    private suspend inline fun fetchAndCache(crossinline block: suspend () -> Unit) =
        suspendRunCatching { block() }.onFailure { e ->
            if (networkConnectivityService.isConnected()) throw e
            Timber.e(e, "Bus remote fetch failed with no network connection, falling back to cache")
        }
}
