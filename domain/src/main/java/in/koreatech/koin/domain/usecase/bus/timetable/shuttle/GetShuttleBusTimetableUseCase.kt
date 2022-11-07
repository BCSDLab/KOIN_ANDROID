package `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle

import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusRoute
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetShuttleBusTimetableUseCase @Inject constructor(
    private val busRepository: BusRepository
) {
    suspend operator fun invoke(
        busCourse: BusCourse,
        busRouteName: String
    ): List<BusNodeInfo.ShuttleNodeInfo> {
        return busRepository.getShuttleBusTimetable(busCourse)
            .find { it.routeName == busRouteName }?.arrivalInfo
            ?: throw NullPointerException("Not found bus timetable")
    }
}