package `in`.koreatech.koin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM ${DBConstant.WEATHER} WHERE id = ${WeatherEntity.ID}")
    suspend fun getWeather(): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)
}
