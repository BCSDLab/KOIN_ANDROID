package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

/**
 * 학기에 포함된 시간표들을 모두 삭제함
 */
class DeleteSemesterUseCase
    @Inject
    constructor(
        private val timetableRepository: TimetableRepository,
    ) {
        suspend operator fun invoke(semester: String): Result<Unit> {
            return timetableRepository.deleteAllTimetableFrame(semester)
        }
    }
