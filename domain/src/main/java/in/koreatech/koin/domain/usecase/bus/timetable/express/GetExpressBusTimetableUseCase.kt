package `in`.koreatech.koin.domain.usecase.bus.timetable.express

import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusRoute
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetExpressBusTimetableUseCase @Inject constructor(
    private val busRepository: BusRepository
) {
    suspend operator fun invoke(busCourse: BusCourse) : List<BusNodeInfo.ExpressNodeInfo> {
        return busRepository.getExpressBusTimetable(busCourse).arrivalInfo
    }
}