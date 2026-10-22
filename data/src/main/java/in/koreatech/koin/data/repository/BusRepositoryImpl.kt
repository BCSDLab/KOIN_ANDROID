package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.core.network.service.NetworkConnectivityService
import `in`.koreatech.koin.data.entity.bus.BusNoticeEntity
import `in`.koreatech.koin.data.entity.bus.CityTimetableEntity
import `in`.koreatech.koin.data.entity.bus.ExpressTimetableEntity
import `in`.koreatech.koin.data.entity.bus.ShuttleCoursesEntity
import `in`.koreatech.koin.data.entity.bus.ShuttleTimetableEntity
import `in`.koreatech.koin.data.source.local.BusLocalDataSource
import `in`.koreatech.koin.data.source.local.CacheLocalDataSource
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
import kotlin.time.Duration.Companion.hours
import timber.log.Timber

class BusRepositoryImpl @Inject constructor(
    private val busRemoteDataSource: BusRemoteDataSource,
    private val busLocalDataSource: BusLocalDataSource,
    private val cacheLocalDataSource: CacheLocalDataSource,
    private val networkConnectivityService: NetworkConnectivityService
) : BusRepository {
    override suspend fun fetchBusNotice(): Result<BusNotice> {
        return suspendRunCatching {
            fetchAndCache(BusNoticeEntity.cacheKey()) {
                val notice = busRemoteDataSource.fetchBusNotice()
                busLocalDataSource.saveBusNotice(notice)
            }
            busLocalDataSource.getBusNotice().toBusNotice()
        }
    }

    override suspend fun fetchShuttleTimetable(id: String): Result<ShuttleTimetable> {
        return suspendRunCatching {
            fetchAndCache(ShuttleTimetableEntity.cacheKey(id)) {
                val timetable = busRemoteDataSource.fetchShuttleTimetable(id)
                busLocalDataSource.saveShuttleTimetable(id, timetable)
            }
            busLocalDataSource.getShuttleTimetable(id).toShuttleTimetable()
        }
    }

    override suspend fun fetchShuttleCourses(): Result<ShuttleCourses> {
        return suspendRunCatching {
            fetchAndCache(ShuttleCoursesEntity.cacheKey()) {
                val courses = busRemoteDataSource.fetchShuttleCourses()
                busLocalDataSource.saveShuttleCourses(courses)
            }
            busLocalDataSource.getShuttleCourses().toShuttleCourses()
        }
    }

    override suspend fun fetchExpressTimetable(direction: String): Result<ExpressTimetable> {
        return suspendRunCatching {
            fetchAndCache(ExpressTimetableEntity.cacheKey(direction)) {
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
            fetchAndCache(CityTimetableEntity.cacheKey(number, direction)) {
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

    private suspend inline fun fetchAndCache(cacheKey: String, crossinline block: suspend () -> Unit) {
        val cachedTime = cacheLocalDataSource.getCachedTime(cacheKey)
        val now = System.currentTimeMillis()
        val isCacheValid = cachedTime != null && (now - cachedTime) < CACHE_TTL.inWholeMilliseconds
        if (isCacheValid) return

        suspendRunCatching { block() }
            .onSuccess { cacheLocalDataSource.updateCachedTime(cacheKey) }
            .onFailure { e ->
                if (networkConnectivityService.isConnected()) throw e
                Timber.e(e, "Bus remote fetch failed with no network connection, falling back to cache")
            }
    }

    companion object {
        private val CACHE_TTL = 1.hours
    }
}
