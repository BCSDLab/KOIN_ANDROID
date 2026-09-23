package `in`.koreatech.koin.domain.usecase.home

import `in`.koreatech.koin.domain.repository.DiningRepository
import `in`.koreatech.koin.domain.repository.WeatherRepository
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SyncHomeUseCase @Inject constructor(
    private val diningRepository: DiningRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): Result<Unit> = suspendRunCatching {
        coroutineScope {
            val weather = async { weatherRepository.getWeather(forceRefresh = true).getOrThrow() }
            val dining = async { diningRepository.fetchDining(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate())).getOrThrow() }

            weather.await()
            dining.await()
        }
    }
}
