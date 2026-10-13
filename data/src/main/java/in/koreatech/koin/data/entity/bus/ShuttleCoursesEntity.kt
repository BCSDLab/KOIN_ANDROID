package `in`.koreatech.koin.data.entity.bus

import androidx.room.Entity
import androidx.room.PrimaryKey
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(tableName = DBConstant.SHUTTLE_COURSES)
data class ShuttleCoursesEntity(
    @PrimaryKey val id: Int = ID,
    val payload: String
) {
    companion object {
        const val ID = 1 // SingTon Row 이므로 1로 고정
    }
}
