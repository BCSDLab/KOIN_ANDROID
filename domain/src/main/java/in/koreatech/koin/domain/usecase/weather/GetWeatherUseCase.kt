package `in`.koreatech.koin.domain.usecase.weather

import `in`.koreatech.koin.domain.model.weather.Weather
import `in`.koreatech.koin.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): Result<Weather> = weatherRepository.getWeather()
}
