package `in`.koreatech.koin.domain.usecase.home

import `in`.koreatech.koin.domain.repository.DiningRepository
import `in`.koreatech.koin.domain.repository.WeatherRepository
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import javax.inject.Inject

class SyncHomeUseCase @Inject constructor(
    private val diningRepository: DiningRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        weatherRepository.getWeather(forceRefresh = true).getOrThrow()
        diningRepository.fetchDining(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate())).getOrThrow()
    }
}
