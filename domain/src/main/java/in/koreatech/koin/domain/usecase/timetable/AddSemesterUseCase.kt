package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameCreateQuery
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class AddSemesterUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {

    suspend operator fun invoke(semester: String): Result<TimetableFrame> {
        return timetableRepository.postTimetableFrame(TimetableFrameCreateQuery(
            semester = semester,
            null
        ))
    }
}