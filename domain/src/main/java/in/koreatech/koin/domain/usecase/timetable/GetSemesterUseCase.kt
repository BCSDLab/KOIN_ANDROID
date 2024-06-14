package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.Semester
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class GetSemesterUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository,
) {
    suspend operator fun invoke(): List<Semester> =
        try {
            timetableRepository.loadSemesters()
        } catch (e: Exception) {
            emptyList()
        }
}