package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.weather.WeatherResponse
import retrofit2.http.GET

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(): WeatherResponse
}
