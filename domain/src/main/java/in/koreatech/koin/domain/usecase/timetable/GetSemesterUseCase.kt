package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.response.Semester
import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSemesterUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    operator fun invoke(): Flow<List<Semester>> =
        timetableRepository.getSemesters()
}