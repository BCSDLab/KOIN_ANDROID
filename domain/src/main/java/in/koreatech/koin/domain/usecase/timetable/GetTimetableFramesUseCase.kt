package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTimetableFramesUseCase
    @Inject
    constructor(
        private val timetableRepository: TimetableRepository,
    ) {
        suspend operator fun invoke(semester: String): Flow<List<TimetableFrame>> = timetableRepository.getTimetableFrames(semester)
    }
