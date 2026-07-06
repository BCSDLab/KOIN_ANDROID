package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class GetTimetableLecturesUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(frameId: Int) =
        timetableRepository.getTimetableLectures(frameId)
}
