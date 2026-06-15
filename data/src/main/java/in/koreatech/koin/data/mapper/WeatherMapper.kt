package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.weather.WeatherResponse
import `in`.koreatech.koin.domain.model.weather.Weather

fun WeatherResponse.toWeather() = Weather(
    temperature = temperature,
    weather = weather,
    weatherId = weatherId,
    weatherIconUrl = weatherIconUrl
)
