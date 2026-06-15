package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.weather.Weather

interface WeatherRepository {
    suspend fun getWeather(): Result<Weather>
}
