package `in`.koreatech.koin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.entity.bus.BusNoticeEntity
import `in`.koreatech.koin.data.entity.bus.CityTimetableEntity
import `in`.koreatech.koin.data.entity.bus.ExpressTimetableEntity
import `in`.koreatech.koin.data.entity.bus.ShuttleCoursesEntity
import `in`.koreatech.koin.data.entity.bus.ShuttleTimetableEntity

@Dao
interface BusDao {
    @Query("SELECT * FROM ${DBConstant.BUS_NOTICE} WHERE id = ${BusNoticeEntity.ID}")
    suspend fun getBusNotice(): BusNoticeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusNotice(entity: BusNoticeEntity)

    @Query("SELECT * FROM ${DBConstant.SHUTTLE_TIMETABLE} WHERE id = :id")
    suspend fun getShuttleTimetable(id: String): ShuttleTimetableEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShuttleTimetable(entity: ShuttleTimetableEntity)

    @Query("SELECT * FROM ${DBConstant.SHUTTLE_COURSES} WHERE id = ${ShuttleCoursesEntity.ID}")
    suspend fun getShuttleCourses(): ShuttleCoursesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShuttleCourses(entity: ShuttleCoursesEntity)

    @Query("SELECT * FROM ${DBConstant.EXPRESS_TIMETABLE} WHERE direction = :direction")
    suspend fun getExpressTimetable(direction: String): ExpressTimetableEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpressTimetable(entity: ExpressTimetableEntity)

    @Query("SELECT * FROM ${DBConstant.CITY_TIMETABLE} WHERE busNumber = :busNumber AND direction = :direction")
    suspend fun getCityTimetable(busNumber: Int, direction: String): CityTimetableEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityTimetable(entity: CityTimetableEntity)
}
