package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class UpdateTimetablesUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(
        semester: String,
        isAnonymous: Boolean,
        lectures: List<Lecture>,
    ) {
        timetableRepository.updateTimetables(semester, isAnonymous, lectures)
    }
}