package `in`.koreatech.koin.feature.home.model

import `in`.koreatech.koin.domain.model.weather.Weather

data class LocalWeather(
    val temperature: Int,
    val weather: String,
    val weatherIconUrl: String
)

internal fun Weather.toLocalWeather() = LocalWeather(
    temperature = temperature,
    weather = weather,
    weatherIconUrl = weatherIconUrl
)
