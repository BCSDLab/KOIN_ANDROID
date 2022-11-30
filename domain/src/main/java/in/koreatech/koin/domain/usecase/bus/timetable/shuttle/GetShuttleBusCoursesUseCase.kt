package `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetShuttleBusCoursesUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    suspend operator fun invoke(): Pair<List<Pair<BusCourse, String>>?, ErrorHandler?> {
        val sort = listOf("천안", "청주", "서울", "대전", "세종")
        return try {
            busRepository.getShuttleBusCourses()
                .sortedWith { p0, p1 ->
                    (sort.indexOf(p0.first.region) - sort.indexOf(p1.first.region)).let {
                        if (it == 0) {
                            p0.first.busType.importance - p1.first.busType.importance
                        } else it
                    }
                } to null
        } catch (t: Throwable) {
            null to busErrorHandler.handleGetBusCoursesError(t)
        }
    }
}

private val BusType.importance get() = when(this) {
    BusType.City -> 4
    BusType.Commuting -> 1
    BusType.Express -> 3
    BusType.Shuttle -> 2
}