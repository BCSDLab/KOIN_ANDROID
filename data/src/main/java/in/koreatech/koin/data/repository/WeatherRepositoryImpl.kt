package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toWeatherEntity
import `in`.koreatech.koin.data.mapper.toWeather
import `in`.koreatech.koin.data.source.local.WeatherLocalDataSource
import `in`.koreatech.koin.data.source.remote.WeatherRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.weather.KoinWeatherException
import `in`.koreatech.koin.domain.model.weather.Weather
import `in`.koreatech.koin.domain.repository.WeatherRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource
) : WeatherRepository {
    override suspend fun getWeather(forceRefresh: Boolean): Result<Weather> {
        if (!forceRefresh) weatherLocalDataSource.getWeather()?.toWeather()?.let { return Result.success(it) }
        return suspendRunCatching {
            weatherRemoteDataSource.getWeather().toWeather().also { weatherLocalDataSource.saveWeather(it.toWeatherEntity()) }
        }.mapHttpFailure {
            on(500, "EXTERNAL_API_ERROR") throws KoinWeatherException.ExternalApiErrorException()
        }
    }
}
