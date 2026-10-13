package `in`.koreatech.koin.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(tableName = DBConstant.SHUTTLE_TIMETABLE)
data class ShuttleTimetableEntity(
    @PrimaryKey val id: String,
    val payload: String
)
