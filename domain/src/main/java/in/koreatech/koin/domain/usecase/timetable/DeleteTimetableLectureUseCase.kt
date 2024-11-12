package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class DeleteTimetableLectureUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return timetableRepository.deleteTimetableLecture(id)
    }
}