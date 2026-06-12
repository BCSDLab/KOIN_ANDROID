package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toWeather
import `in`.koreatech.koin.data.source.remote.WeatherRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.data.util.suspendRunCatching
import `in`.koreatech.koin.domain.error.weather.KoinWeatherException
import `in`.koreatech.koin.domain.model.weather.Weather
import `in`.koreatech.koin.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {
    override suspend fun getWeather(): Result<Weather> {
        return suspendRunCatching {
            weatherRemoteDataSource.getWeather().toWeather()
        }.mapHttpFailure {
            on(500, "EXTERNAL_API_ERROR") throws KoinWeatherException.ExternalApiErrorException()
        }
    }
}
