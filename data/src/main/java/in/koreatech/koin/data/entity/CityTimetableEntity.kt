package `in`.koreatech.koin.data.entity

import androidx.room.Entity
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(
    tableName = DBConstant.CITY_TIMETABLE,
    primaryKeys = ["busNumber", "direction"]
)
data class CityTimetableEntity(
    val busNumber: Int,
    val direction: String,
    val payload: String
)
