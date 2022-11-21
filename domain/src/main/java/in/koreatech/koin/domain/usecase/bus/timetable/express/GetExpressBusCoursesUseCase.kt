package `in`.koreatech.koin.domain.usecase.bus.timetable.express

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetExpressBusCoursesUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    suspend operator fun invoke(): Pair<List<Pair<BusCourse, String>>?, ErrorHandler?> {
        return try {
            busRepository.getExpressBusCourses() to null
        } catch (t: Throwable) {
            null to busErrorHandler.handleGetBusTimetableError(t)
        }
    }
}