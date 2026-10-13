package `in`.koreatech.koin.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(tableName = DBConstant.EXPRESS_TIMETABLE)
data class ExpressTimetableEntity(
    @PrimaryKey val direction: String,
    val payload: String
)
