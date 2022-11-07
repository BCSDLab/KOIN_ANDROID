package `in`.koreatech.koin.domain.usecase.bus.timetable.city

import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusRoute
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetCityBusTimetableUseCase @Inject constructor(
    private val busRepository: BusRepository
) {
    suspend operator fun invoke() : List<BusNodeInfo.CitybusNodeInfo> {
        return busRepository.getCityBusTimetable().arrivalInfo
    }
}