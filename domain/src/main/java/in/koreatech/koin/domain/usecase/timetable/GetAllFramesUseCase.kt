package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllFramesUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(): Flow<Map<String, List<TimetableFrame>>> = timetableRepository.getAllFrames()
}
