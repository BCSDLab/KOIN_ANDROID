package `in`.koreatech.koin.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(tableName = DBConstant.WEATHER)
data class WeatherEntity(
    @PrimaryKey val id: Int = ID,
    val temperature: Int,
    val weather: String,
    val weatherId: Int,
    val weatherIconUrl: String
) {
    companion object {
        const val ID = 1
    }
}
