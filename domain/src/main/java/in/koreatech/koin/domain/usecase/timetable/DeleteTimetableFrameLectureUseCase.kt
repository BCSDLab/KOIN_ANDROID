package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class DeleteTimetableFrameLectureUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(frameId: Int, lectureId: Int): Result<Unit> {
        return timetableRepository.deleteTimetableFrameLecture(frameId, lectureId)
    }
}