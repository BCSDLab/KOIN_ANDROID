package `in`.koreatech.koin.domain.usecase.timetable

import `in`.koreatech.koin.domain.model.timetable.toLegacySemester
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 유저가 추가한 학기 리스트
 * @param isAnonymouse 익명 여부
 */
class GetUserSemestersUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    operator fun invoke(isAnonymous: Boolean): Flow<List<String>> =
        if (isAnonymous) {
            timetableRepository.getSemesters().map { it.map { it.toLegacySemester() } }
        } else {
            timetableRepository.getUserSemesters().map { it.map { it.toLegacySemester() } }
        }
}
