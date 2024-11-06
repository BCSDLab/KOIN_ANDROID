package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSemesterListUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return timetableRepository.getSemesterCheck()
    }
}