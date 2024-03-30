package `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.timetable.BusTimetable
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetShuttleBusTimetableUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    suspend operator fun invoke(
        busCourse: BusCourse,
    ): Pair<BusTimetable.ShuttleBusTimetable?, ErrorHandler?> {
        return try {
            busRepository.getShuttleBusTimetable(busCourse) to null
        } catch(t: Throwable) {
            null to busErrorHandler.handleGetBusTimetableError(t)
        }
    }
}
