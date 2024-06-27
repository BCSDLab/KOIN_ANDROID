package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class GetTimetablesUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository,
) {
    suspend operator fun invoke(
        semester: String,
        isAnonymous: Boolean,
    ): List<Lecture> = timetableRepository.getTimetables(semester, isAnonymous)
}