package `in`.koreatech.koin.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(tableName = DBConstant.BUS_NOTICE)
data class BusNoticeEntity(
    @PrimaryKey val id: Int = ID,
    val payload: String
) {
    companion object {
        const val ID = 1
    }
}
