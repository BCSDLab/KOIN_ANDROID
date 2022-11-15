package `in`.koreatech.koin.domain.usecase.bus.timetable.city

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetCityBusTimetableUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    suspend operator fun invoke() : Pair<List<BusNodeInfo.CitybusNodeInfo>?, ErrorHandler?> {
        return try {
            busRepository.getCityBusTimetable().arrivalInfo to null
        } catch (t: Throwable) {
            null to busErrorHandler.handleGetBusTimetableError(t)
        }
    }
}