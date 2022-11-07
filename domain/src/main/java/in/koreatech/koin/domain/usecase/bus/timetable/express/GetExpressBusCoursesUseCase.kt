package `in`.koreatech.koin.domain.usecase.bus.timetable.express

import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetExpressBusCoursesUseCase @Inject constructor(
    private val busRepository: BusRepository
) {
    suspend operator fun invoke(): List<Pair<BusCourse, String>> {
        return busRepository.getExpressBusCourses()
    }
}