package `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle

import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.timetable.BusRoute
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetShuttleBusRoutesUseCase @Inject constructor(
    private val busRepository: BusRepository
) {
    suspend operator fun invoke(busCourse: BusCourse): List<String> {
        return busRepository.getShuttleBusTimetable(busCourse).map { it.routeName }
    }
}