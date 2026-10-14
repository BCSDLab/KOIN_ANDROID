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
import kotlinx.coroutines.CancellationException
import timber.log.Timber

class BusRepositoryImpl @Inject constructor(
    private val busRemoteDataSource: BusRemoteDataSource,
    private val busLocalDataSource: BusLocalDataSource,
    private val networkConnectivityService: NetworkConnectivityService
) : BusRepository {
    override suspend fun fetchBusNotice(): Result<BusNotice> {
        return suspendRunCatching {
            if (networkConnectivityService.isConnected()) {
                val notice = busRemoteDataSource.fetchBusNotice()
                ignoringCacheFailure { busLocalDataSource.saveBusNotice(notice) }
                notice.toBusNotice()
            } else {
                busLocalDataSource.getBusNotice().toBusNotice()
            }
        }
    }

    override suspend fun fetchShuttleTimetable(id: String): Result<ShuttleTimetable> {
        return suspendRunCatching {
            if (networkConnectivityService.isConnected()) {
                val timetable = busRemoteDataSource.fetchShuttleTimetable(id)
                ignoringCacheFailure { busLocalDataSource.saveShuttleTimetable(id, timetable) }
                timetable.toShuttleTimetable()
            } else {
                busLocalDataSource.getShuttleTimetable(id).toShuttleTimetable()
            }
        }
    }

    override suspend fun fetchShuttleCourses(): Result<ShuttleCourses> {
        return suspendRunCatching {
            if (networkConnectivityService.isConnected()) {
                val courses = busRemoteDataSource.fetchShuttleCourses()
                ignoringCacheFailure { busLocalDataSource.saveShuttleCourses(courses) }
                courses.toShuttleCourses()
            } else {
                busLocalDataSource.getShuttleCourses().toShuttleCourses()
            }
        }
    }

    override suspend fun fetchExpressTimetable(direction: String): Result<ExpressTimetable> {
        return suspendRunCatching {
            if (networkConnectivityService.isConnected()) {
                val timetable = busRemoteDataSource.fetchExpressTimetable(direction)
                ignoringCacheFailure { busLocalDataSource.saveExpressTimetable(direction, timetable) }
                timetable.toExpressTimetable()
            } else {
                busLocalDataSource.getExpressTimetable(direction).toExpressTimetable()
            }
        }
    }

    override suspend fun fetchCityTimetable(
        number: Int,
        direction: String
    ): Result<CityTimetable> {
        return suspendRunCatching {
            if (networkConnectivityService.isConnected()) {
                val timetable = busRemoteDataSource.fetchCityTimetable(number, direction)
                ignoringCacheFailure { busLocalDataSource.saveCityTimetable(number, direction, timetable) }
                timetable.toCityTimetable()
            } else {
                busLocalDataSource.getCityTimetable(number, direction).toCityTimetable()
            }
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

    @Suppress("TooGenericExceptionCaught")
    private suspend inline fun ignoringCacheFailure(crossinline block: suspend () -> Unit) {
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e("Bus cache write failed: ${e.message}")
        }
    }
}
