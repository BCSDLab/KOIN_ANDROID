package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.dao.WeatherDao
import `in`.koreatech.koin.data.entity.WeatherEntity
import javax.inject.Inject

class WeatherLocalDataSource @Inject constructor(
    private val weatherDao: WeatherDao
) {
    suspend fun getWeather(): WeatherEntity? = weatherDao.getWeather()
    suspend fun saveWeather(weather: WeatherEntity) = weatherDao.insertWeather(weather)
}
