package `in`.koreatech.koin.domain.usecase.bus.timetable.city

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject
import `in`.koreatech.koin.domain.model.Result
import `in`.koreatech.koin.domain.model.toResult

class GetCityBusTimetableUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    suspend operator fun invoke() : Result<List<BusNodeInfo.CitybusNodeInfo>> {
        return try {
            busRepository.getCityBusTimetable().arrivalInfo.toResult()
        } catch (t: Throwable) {
            busErrorHandler.handleGetBusTimetableError(t)
        }
    }
}