package `in`.koreatech.koin.domain.usecase.bus.timetable.city

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.city.CityBusInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusTimetable
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetCityBusTimetableUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    suspend operator fun invoke(cityBusInfo: CityBusInfo) : Pair<BusTimetable.CityBusTimetable?, ErrorHandler?> {
        return try {
            busRepository.getCityBusTimetable(cityBusInfo) to null
        } catch (t: Throwable) {
            null to busErrorHandler.handleGetBusTimetableError(t)
        }
    }
}