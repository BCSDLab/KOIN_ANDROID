package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.WeatherApi
import `in`.koreatech.koin.data.response.weather.WeatherResponse
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
) {
    suspend fun getWeather(): WeatherResponse = weatherApi.getWeather()
}
