package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class GetDepartmentsUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository,
) {
    suspend operator fun invoke(): List<Department> =
        try {
            timetableRepository.loadDepartments()
        } catch (e: Exception) {
            emptyList()
        }
}