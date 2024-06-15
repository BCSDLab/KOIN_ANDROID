package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTimetablesUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository,
) {
    suspend operator fun invoke(
        semester: String,
    ): List<Lecture> = timetableRepository.getTimetables(semester)
}