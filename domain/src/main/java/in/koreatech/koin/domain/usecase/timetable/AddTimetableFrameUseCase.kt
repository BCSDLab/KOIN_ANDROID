package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameCreateQuery
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class AddTimetableFrameUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    // TODO::hyeok 시간표 이름 인자에서 없애고 내부에서 처리하는게 나을듯
    suspend operator fun invoke(semester: String, timetableName: String): Result<TimetableFrame> {
        return timetableRepository.postTimetableFrame(TimetableFrameCreateQuery(
            semester = semester,
            timetableName = timetableName
        ))
    }
}