package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.request.LecturesQuery
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class AddTimetableLectureUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(frameId: Int, lectures: List<Lecture>): Result<TimetableLectures> {
        val query = LecturesQuery(
            timetableFrameId = frameId,
            timetableLecture = lectures.map { it.toLectureQuery() }
        )
        return timetableRepository.postTimetableLectures(query)
    }
}