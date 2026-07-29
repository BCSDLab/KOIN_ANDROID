package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class GetLocalTimetableLecturesUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(semester: String): Result<TimetableLectures> =
        timetableRepository.getTimetableLectures(semester)
}
