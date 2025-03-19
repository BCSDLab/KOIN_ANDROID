package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.toLegacySemester
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 현재 추가 가능한 학기 리스트
 */
class GetSemestersUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    operator fun invoke(): Flow<List<String>> = timetableRepository.getSemesters().map { it.map { it.toLegacySemester() } }
}
