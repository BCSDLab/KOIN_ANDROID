package `in`.koreatech.koin.data.source.local

import com.google.gson.Gson
import `in`.koreatech.koin.data.dao.BusDao
import `in`.koreatech.koin.data.entity.bus.BusNoticeEntity
import `in`.koreatech.koin.data.entity.bus.CityTimetableEntity
import `in`.koreatech.koin.data.entity.bus.ExpressTimetableEntity
import `in`.koreatech.koin.data.entity.bus.ShuttleCoursesEntity
import `in`.koreatech.koin.data.entity.bus.ShuttleTimetableEntity
import `in`.koreatech.koin.data.response.bus.BusNoticeResponse
import `in`.koreatech.koin.data.response.bus.CityTimetableResponse
import `in`.koreatech.koin.data.response.bus.ExpressTimetableResponse
import `in`.koreatech.koin.data.response.bus.ShuttleCoursesResponse
import `in`.koreatech.koin.data.response.bus.ShuttleTimetableResponse
import `in`.koreatech.koin.data.source.datastore.BusDataStore
import javax.inject.Inject

@Suppress("Detekt.TooManyFunctions")
class BusLocalDataSource @Inject constructor(
    private val busDao: BusDao,
    private val busDataStore: BusDataStore
) {
    suspend fun getBusNotice(): BusNoticeResponse {
        val entity = busDao.getBusNotice() ?: throw NoSuchElementException("No cached bus notice")
        return Gson().fromJson(entity.payload, BusNoticeResponse::class.java)
    }

    suspend fun saveBusNotice(response: BusNoticeResponse) {
        busDao.insertBusNotice(BusNoticeEntity(payload = Gson().toJson(response)))
    }

    suspend fun getShuttleTimetable(id: String): ShuttleTimetableResponse {
        val entity = busDao.getShuttleTimetable(id) ?: throw NoSuchElementException("No cached shuttle timetable for $id")
        return Gson().fromJson(entity.payload, ShuttleTimetableResponse::class.java)
    }

    suspend fun saveShuttleTimetable(id: String, response: ShuttleTimetableResponse) {
        busDao.insertShuttleTimetable(ShuttleTimetableEntity(id = id, payload = Gson().toJson(response)))
    }

    suspend fun getShuttleCourses(): ShuttleCoursesResponse {
        val entity = busDao.getShuttleCourses() ?: throw NoSuchElementException("No cached shuttle courses")
        return Gson().fromJson(entity.payload, ShuttleCoursesResponse::class.java)
    }

    suspend fun saveShuttleCourses(response: ShuttleCoursesResponse) {
        busDao.insertShuttleCourses(ShuttleCoursesEntity(payload = Gson().toJson(response)))
    }

    suspend fun getExpressTimetable(direction: String): ExpressTimetableResponse {
        val entity = busDao.getExpressTimetable(direction) ?: throw NoSuchElementException("No cached express timetable for $direction")
        return Gson().fromJson(entity.payload, ExpressTimetableResponse::class.java)
    }

    suspend fun saveExpressTimetable(direction: String, response: ExpressTimetableResponse) {
        busDao.insertExpressTimetable(ExpressTimetableEntity(direction = direction, payload = Gson().toJson(response)))
    }

    suspend fun getCityTimetable(
        busNumber: Int,
        direction: String
    ): CityTimetableResponse {
        val entity = busDao.getCityTimetable(busNumber, direction)
            ?: throw NoSuchElementException("No cached city timetable for $busNumber $direction")
        return Gson().fromJson(entity.payload, CityTimetableResponse::class.java)
    }

    suspend fun saveCityTimetable(
        busNumber: Int,
        direction: String,
        response: CityTimetableResponse
    ) {
        busDao.insertCityTimetable(
            CityTimetableEntity(busNumber = busNumber, direction = direction, payload = Gson().toJson(response))
        )
    }

    suspend fun getLastShownNoticeId(): Int {
        return busDataStore.getLastShownNoticeId()
    }

    suspend fun saveLastShownNoticeId(id: Int) {
        busDataStore.saveLastShownNoticeId(id)
    }
}
