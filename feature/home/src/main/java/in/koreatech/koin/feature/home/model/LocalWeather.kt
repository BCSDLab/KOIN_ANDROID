package `in`.koreatech.koin.feature.home.model

import `in`.koreatech.koin.domain.model.weather.Weather

data class LocalWeather(
    val temperature: Int,
    val weather: String,
    val weatherId: Int,
    val weatherIconUrl: String
)

internal fun Weather.toLocalWeather() = LocalWeather(
    temperature = temperature,
    weather = weather,
    weatherId = weatherId,
    weatherIconUrl = weatherIconUrl
)
