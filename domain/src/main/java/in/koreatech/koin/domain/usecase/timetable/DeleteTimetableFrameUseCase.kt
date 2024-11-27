package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class DeleteTimetableFrameUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {

    suspend operator fun invoke(frameId: Int): Result<Unit> {
        return timetableRepository.deleteTimetableFrame(frameId)
    }
}