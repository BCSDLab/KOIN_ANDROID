package `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusRoute
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject
import `in`.koreatech.koin.domain.model.Result
import `in`.koreatech.koin.domain.model.toResult

class GetShuttleBusTimetableUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    suspend operator fun invoke(
        busCourse: BusCourse,
        busRouteName: String
    ): Result<List<BusNodeInfo.ShuttleNodeInfo>> {
        return try {
            val timetable = busRepository.getShuttleBusTimetable(busCourse)
                .find { it.routeName == busRouteName }?.arrivalInfo
                ?: throw NullPointerException("Not found bus timetable")
            timetable.toResult()
        } catch (t: Throwable) {
            busErrorHandler.handleGetBusTimetableError(t)
        }
    }
}