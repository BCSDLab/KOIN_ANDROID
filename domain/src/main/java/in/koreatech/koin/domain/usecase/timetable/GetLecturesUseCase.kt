package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLecturesUseCase
    @Inject
    constructor(
        private val timetableRepository: TimetableRepository,
    ) {
        operator fun invoke(semesterDate: String): Flow<List<Lecture>> = timetableRepository.getLectures(semesterDate)
    }
